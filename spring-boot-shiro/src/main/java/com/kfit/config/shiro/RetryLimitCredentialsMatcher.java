package com.kfit.config.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description 验证器,增加了登录次数校验
 * @Author lujichao
 * @Date 2019/2/18
 */
@Component
public class RetryLimitCredentialsMatcher extends SimpleCredentialsMatcher {
    private static final Logger logger = LoggerFactory.getLogger(RetryLimitCredentialsMatcher.class);

    /**
     * 从配置中读取重试次数
     */
    @Value("${login.maxRetryNum}")
    private int maxRetryNum;

    private EhCacheManager ehCacheManager;

    public void setMaxRetryNum(int maxRetryNum) {
        this.maxRetryNum = maxRetryNum;
    }

    public RetryLimitCredentialsMatcher(EhCacheManager ehCacheManager){
        this.ehCacheManager = ehCacheManager;
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        Cache<String, AtomicInteger> passwordRetryCache = ehCacheManager.getCache("passwordRetryCache");
        String username = (String) token.getPrincipal();
        //retry count +1
        AtomicInteger retryCount = passwordRetryCache.get(username);
        if (retryCount == null){
            retryCount = new AtomicInteger(0);
            passwordRetryCache.put(username,retryCount);
        }
        if (retryCount.incrementAndGet()> maxRetryNum){
            logger.warn("用户[{}]进行登录验证..失败验证超过{}次",username,maxRetryNum);
            throw new ExcessiveAttemptsException("username: "+username+"tried to login more than 5 times in period");
        }

        boolean matches = super.doCredentialsMatch(token, info);
        //清除重试数据
        if (matches){
            //clear retry data
            passwordRetryCache.remove(username);
        }

        return matches;
    }
}
