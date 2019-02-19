package com.kfit.config.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import com.google.code.kaptcha.Constants;

/**
 * @Description 验证码拦截器
 * @Author lujichao
 * @Date 2019/2/19
 */
public class CaptchaValidateFilter extends AccessControlFilter {


    private String captchaParam = "captchaCode";//前台提交的验证码参数名

    private String failureKeyAttribute = "shiroLoginFailure";  //验证失败后存储到的属性名

    /**
     * isAccessAllowed：表示是否允许访问；0[urls]配置中拦截器参数部分，如果允许访问返回true，否则false；
     * @param servletRequest
     * @param servletResponse
     * @param o
     * @return
     * @throws Exception
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {

        //从session获取正确的验证码
        Session session = SecurityUtils.getSubject().getSession();

        //页面输入的验证码
        //String captchaCode =getCaptchaCode(servletRequest);
        String captchaCode = getCaptchaParam();

        String validateCode = (String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);

        HttpServletRequest httpServletRequest = WebUtils.toHttp(servletRequest);

        //判断验证码是否表单提交(允许访问)
        if (!"post".equalsIgnoreCase(httpServletRequest.getMethod())){
            return true;
        }
        //若验证码为空或匹配失败则返回false
        if (captchaCode == null){
            return false;
        }else if (validateCode != null){
            captchaCode  = captchaCode.toLowerCase();
            validateCode = validateCode.toLowerCase();
            if (!captchaCode.equals(validateCode)){
                return false;
            }
        }

        return true;
    }

    /**
     * onAccessDenied：表示当访问拒绝时是否已经处理了；如果返回true表示需要继续处理；如果返回false表示该拦截器实例已经处理了，将直接返回即可
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        //如果验证码失败了,存储失败key属性
        servletRequest.setAttribute(failureKeyAttribute,"验证码错误");

        return true;
    }

    public String getCaptchaParam() {
        return captchaParam;
    }

    public void setCaptchaParam(String captchaParam) {
        this.captchaParam = captchaParam;
    }
}
