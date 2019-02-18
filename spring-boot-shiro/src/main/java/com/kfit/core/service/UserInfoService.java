package com.kfit.core.service;

import com.kfit.core.bean.UserInfo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lujichao
 * @since 2019-02-15
 */
public interface UserInfoService {
    /**
     * 通过username查找用户信息
     */
    UserInfo findByUsername(String username);
}
