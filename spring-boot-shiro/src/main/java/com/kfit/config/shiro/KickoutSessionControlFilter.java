package com.kfit.config.shiro;

import com.kfit.core.bean.UserInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.Deque;
import java.util.LinkedList;

/**
 * @Description 并发登录人数控制
 * @Author lujichao
 * @Date 2019/2/18
 */
public class KickoutSessionControlFilter extends AccessControlFilter {

    private static final Logger logger = LoggerFactory.getLogger(KickoutSessionControlFilter.class);

    //踢出后到的地址
    private String kickoutUrl;

    //踢出之前/之后登录的用户,默认踢出之前登录的用户
    private boolean kickoutAfter = false;

    //同一账号最大会话数
    private int maxSession = 1;

    private String kickoutAttrName = "kickout";

    private SessionManager sessionManager;

    private Cache<String, Deque<Serializable>> cache;

    public void setKickoutUrl(String kickoutUrl) {
        this.kickoutUrl = kickoutUrl;
    }

    public void setKickoutAfter(boolean kickoutAfter) {
        this.kickoutAfter = kickoutAfter;
    }

    public void setMaxSession(int maxSession) {
        this.maxSession = maxSession;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * 设置Cache的key前缀
     *
     * @param cacheManager
     */
    public void setCacheManager(CacheManager cacheManager) {
        this.cache = cacheManager.getCache("shiro-=kickout-session");
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        Subject subject = getSubject(servletRequest, servletResponse);
        if (!subject.isAuthenticated() && !subject.isRemembered()) {
            //如果没有登录,直接进行之后的流程
            return true;
        }
        Session session = subject.getSession();

        UserInfo userInfo = (UserInfo) subject.getPrincipal();

        String username = userInfo.getUsername();

        Serializable sessionId = session.getId();

        logger.info("进入KickoutController,seesionId:{}", sessionId);

        //读取缓存
        Deque<Serializable> deque = cache.get(username);

        if (deque.isEmpty()) {
            deque = new LinkedList<>();
            cache.put(username, deque);
        }

        //如果队列里没有此sessionId,且用户没有被踢出;放入队列
        if (!deque.contains(sessionId) && session.getAttribute(kickoutAttrName) == null) {
            //将session存入队列
            deque.push(sessionId);
        }

        logger.info("deque.size:{}", deque.size());

        //如果队列里的sessionId超过最大会话叔,开始踢人
        while (deque.size() > maxSession) {
            Serializable kickoutSessionId = null;
            if (kickoutAfter) {
                //踢出后者
                kickoutSessionId = deque.removeFirst();
            } else {
                //否则踢出前者
                kickoutSessionId = deque.removeLast();
            }

            //踢出后更新缓存队列
            cache.put(username, deque);
            try {
                //获取被踢出的sessionId的session对象
                Session kickoutSession = sessionManager.getSession(new DefaultSessionKey(kickoutSessionId));
                if (kickoutSession != null){
                    //设置kickout属性表示踢出了
                    kickoutSession.setAttribute(kickoutAttrName,true);
                }

            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
        }
        //如果被踢出,直接退出,重定向到踢出的地址
        if (session.getAttribute(kickoutAttrName) != null && (boolean)session.getAttribute(kickoutAttrName)== true){
            //会话被踢出了
            try{
                //退出登录
                subject.logout();
            }catch (Exception e){
                e.printStackTrace();
                logger.warn(e.getMessage());
            }
            saveRequest(servletRequest);
            //重定向
            logger.info("用户登录人数超过限制,重定向到{}",kickoutUrl);
            String reason = URLEncoder.encode("账户已超过登录人数限制","UTF-8");
            String redirectUrl = kickoutUrl + (kickoutUrl.contains("?")?"&":"?")+"shiroLoginFailure="+reason;
            WebUtils.issueRedirect(servletRequest,servletResponse,redirectUrl);
            return false;
        }

        return false;
    }
}
