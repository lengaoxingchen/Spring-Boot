package com.kfit.core.service.impl;


import com.kfit.core.bean.UserInfo;
import com.kfit.core.repository.UserInfoRepository;
import com.kfit.core.service.UserInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lujichao
 * @since 2019-02-15
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Resource
    private UserInfoRepository userInfoRepository;

    @Override
    public UserInfo findByUsername(String username) {
        System.out.println("UserInfoServiceImpl.findByUsername");
        return userInfoRepository.findByUsername(username);
    }
}
