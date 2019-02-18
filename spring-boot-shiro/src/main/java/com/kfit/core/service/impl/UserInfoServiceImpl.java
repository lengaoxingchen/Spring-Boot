package com.kfit.core.service.impl;


import com.kfit.core.bean.UserInfo;
import com.kfit.core.repository.UserInfoRepository;
import com.kfit.core.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);
    @Resource
    private UserInfoRepository userInfoRepository;

    @Override
    public UserInfo findByUsername(String username) {
        logger.info("UserInfoServiceImpl.findByUsername");
        return userInfoRepository.findByUsername(username);
    }
}
