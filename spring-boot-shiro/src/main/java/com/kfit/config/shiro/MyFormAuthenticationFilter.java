package com.kfit.config.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @Description 自定义表单认证
 * @Author lujichao
 * @Date 2019/2/19
 */
public class MyFormAuthenticationFilter extends FormAuthenticationFilter {
    private static final Logger logger = LoggerFactory.getLogger(MyFormAuthenticationFilter.class);

    /**
     * 校验验证码
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response,Object o) throws Exception {
        if (request.getAttribute(getFailureKeyAttribute()) != null){
            return true;
        }
        return super.onAccessDenied(request, response,o);
    }
    /**
     * 重写该方法,判断返回登录信息
     *
     * @param request
     * @param
     * @return
     * @throws Exception
     */
    @Override
    protected void setFailureAttribute(ServletRequest request, AuthenticationException ae) {
        String className = ae.getClass().getName();

        String message;

        String userName = getUsername(request);
        if (UnknownAccountException.class.getName().equalsIgnoreCase(className)) {
            logger.info("对用户[{}]进行登录验证..验证未通过,未知账户", userName);
            message = "账户不存在";
        } else if (IncorrectCredentialsException.class.getName().equals(className)) {
            logger.info("对用户[{}]进行登录验证..验证未通过,错误的凭证", userName);
            message = "密码不正确";
        } else if (LockedAccountException.class.getName().equals(className)) {
            logger.info("对用户[{}]进行登录验证..验证未通过,用户已锁定", userName);
            message = "账户已锁定";
        } else if (ExcessiveAttemptsException.class.getName().equals(className)) {
            logger.info("对用户[{}]进行登录验证..验证未通过,错误次数过多", userName);
            message = "用户名或密码错误次数过多,请十分钟后再试";
        } else if (AuthenticationException.class.getName().equals(className)) {
            logger.info("对用户[{}]进行登录验证..验证未通过,未知错误", userName);
            message = "用户名或密码不正确";
        } else {
            message = className;
        }
        request.setAttribute(getFailureKeyAttribute(), message);
    }
}
