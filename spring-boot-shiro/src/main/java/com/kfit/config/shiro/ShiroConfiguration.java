package com.kfit.config.shiro;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Description Shiro 配置
 * Apache Shiro 核心通过 Filter 来实现，就好像SpringMvc 通过DispachServlet 来主控制一样。
 * 既然是使用 Filter 一般也就能猜到，是通过URL规则来进行过滤和权限校验，所以我们需要定义一系列关于URL的规则和访问权限。
 * @Author lujichao
 * @Date 2019/2/15
 */

@Configuration
public class ShiroConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(ShiroConfiguration.class);

    @Value("${login.maxRetryNum}")
    private int maxRetryNum;

    /**
     * ShiroFilterFactoryBean 处理拦截资源文件问题。
     * 注意：单独一个ShiroFilterFactoryBean配置是或报错的，以为在
     * 初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager
     * <p>
     * Filter Chain定义说明
     * 1、一个URL可以配置多个Filter，使用逗号分隔
     * 2、当设置多个过滤器时，全部验证通过，才视为通过
     * 3、部分过滤器可指定参数，如perms，roles
     */
    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        logger.info("ShiroConfiguration.shirFilter()");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);


        //拦截器.
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();

        //配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了
        filterChainDefinitionMap.put("/logout", "logout");


        //配置记住我或认证通过可以访问的地址
        filterChainDefinitionMap.put("/index", "user");
        filterChainDefinitionMap.put("/", "user");


        //<!-- 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
        //<!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
        //shiro过滤链中加入并发登录人数过滤器
        filterChainDefinitionMap.put("/**", "authc,user");

        //解决登陆页面点登陆会下载favicon.icon ,页面也没跳转到index.html
        //原因:基本大多数浏览器都会请求 favicon.ico 这个图标文件用来展示在浏览器的URL地址前面，而这个文件被shiro保护了
        //上面使用/**进行拦截了，authc说明所有url都必须认证通过才可以访问
        //解决方案一:只需要加入一个过滤
        filterChainDefinitionMap.put("/favicon.ico", "anon");

        //解决方案二:放开static路径

        //filterChainDefinitionMap.put("/static/*","anon");


        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/login");
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/index");
        //未授权界面;
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }


    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //设置realm.
        securityManager.setRealm(myShiroRealm());

        //注入缓存管理器;
        securityManager.setCacheManager(ehCacheManager());//这个如果执行多次，也是同样的一个对象;

        //注入记住我管理器;
        securityManager.setRememberMeManager(rememberMeManager());

        return securityManager;
    }


    /**
     * 身份认证realm;
     * (这个需要自己写，账号密码校验；权限等)
     *
     * @return
     */
    @Bean
    public MyShiroRealm myShiroRealm() {
        MyShiroRealm myShiroRealm = new MyShiroRealm();

        //添加凭证匹配器
        myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());

        //添加认证器
        myShiroRealm.setCredentialsMatcher(retryLimitCredentialsMatcher());
        return myShiroRealm;
    }

    /**
     * 凭证匹配器
     * （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
     * 所以我们需要修改下doGetAuthenticationInfo中的代码;
     * ）
     *
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();

        hashedCredentialsMatcher.setHashAlgorithmName("md5");//散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashIterations(2);//散列的次数，比如散列两次，相当于 md5(md5(""));

        return hashedCredentialsMatcher;
    }

    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     *
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }


    /**
     * shiro缓存管理器;
     * 需要注入对应的其它的实体类中：
     * 1、安全管理器：securityManager
     * 可见securityManager是整个shiro的核心；
     *
     * @return
     */
    @Bean
    public EhCacheManager ehCacheManager() {
        logger.info("ShiroConfiguration.getEhCacheManager()");
        EhCacheManager cacheManager = new EhCacheManager();
        cacheManager.setCacheManagerConfigFile("classpath:config/ehcache-shiro.xml");
        return cacheManager;
    }

    /**
     * cookie对象;
     *
     * @return
     */
    @Bean
    public SimpleCookie rememberMeCookie() {
        logger.info("ShiroConfiguration.rememberMeCookie()");
        //这个参数是cookie的名称，对应前端的checkbox 的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //<!-- 记住我cookie生效时间30天 ,单位秒;-->
        simpleCookie.setMaxAge(259200);
        return simpleCookie;
    }

    /**
     * cookie管理对象;
     *
     * @return
     */
    @Bean
    public CookieRememberMeManager rememberMeManager() {
        logger.info("ShiroConfiguration.rememberMeManager()");
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        return cookieRememberMeManager;
    }

    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }

    /**
     * 限制登录次数
     *
     * @return 匹配器
     */
    @Bean
    public CredentialsMatcher retryLimitCredentialsMatcher() {
        RetryLimitCredentialsMatcher retryLimitCredentialsMatcher = new RetryLimitCredentialsMatcher(ehCacheManager());
        retryLimitCredentialsMatcher.setMaxRetryNum(maxRetryNum);
        return retryLimitCredentialsMatcher;
    }

    /**
     * 会话管理器
     *
     * @return
     */
    @Bean
    public DefaultWebSessionManager configWebSessionManager() {
        DefaultWebSessionManager manager = new DefaultWebSessionManager();

        //加入缓存管理器
        manager.setCacheManager(ehCacheManager());
        //删除过期的session
        manager.setDeleteInvalidSessions(true);

        //设置全局session超时时间
        manager.setGlobalSessionTimeout(60 * 1000);

        //是否定时检查session
        manager.setSessionValidationSchedulerEnabled(true);
        manager.setSessionValidationScheduler(configSessionValidationScheduler());
        manager.setSessionIdUrlRewritingEnabled(false);
        manager.setSessionIdCookieEnabled(true);

        return manager;

    }

    /**
     * 会话验证调度器
     * @return
     */
    @Bean
    public ExecutorServiceSessionValidationScheduler configSessionValidationScheduler() {
        ExecutorServiceSessionValidationScheduler serviceSessionValidationScheduler = new ExecutorServiceSessionValidationScheduler();
        //设置session失效扫描键哥,单位为毫秒
        serviceSessionValidationScheduler.setInterval(300 * 1000);
        return serviceSessionValidationScheduler;
    }

    /**
     * 限制同一账号登录人数控制
     * @return 过滤器
     */
    @Bean
    public KickoutSessionControlFilter kickoutSessionControlFilter(){
        KickoutSessionControlFilter kickoutSessionControlFilter = new KickoutSessionControlFilter();
        //使用cacheManager获取响应的cache来缓存用户登录的会话;用于保存用户-会话之间的关系
        //这里我们还是用之前的shiro使用的redisManager()实现的cacheManager()缓存管理
        //也可以重新另写一个,重新配置缓存时间之类的自定义缓存属性
        kickoutSessionControlFilter.setCacheManager(ehCacheManager());
        //用于根据会话ID,获取会话进行踢出操作的
        kickoutSessionControlFilter.setSessionManager(configWebSessionManager());
        //是否踢出后来登录的,默认是false;即后者登录的用户提出前者登录的用户;踢出顺序
        kickoutSessionControlFilter.setKickoutAfter(false);
        //同一个用户最大的会话数,默认1;比如2的意思是同一个用户允许最多同事两个人登录
        kickoutSessionControlFilter.setMaxSession(1);

        //被踢出后重定向到的地址
        kickoutSessionControlFilter.setKickoutUrl("/login");

        return kickoutSessionControlFilter;
    }
}