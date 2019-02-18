package com.kfit.core.repository;


import com.kfit.core.bean.UserInfo;
import org.springframework.data.repository.CrudRepository;

/**
 * <p>
 *  userInfo持久化类
 * </p>
 *
 * @author lujichao
 * @since 2019-02-15
 */
public interface UserInfoRepository extends CrudRepository<UserInfo,Long> {

    /**
     * 通过username查找用户信息
     */
    UserInfo findByUsername(String username);
}
