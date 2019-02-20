package com.kfit.repository;

import com.kfit.bean.DemoInfo;
import org.springframework.data.repository.CrudRepository;

/**
 * @Description DemoInfo持久化类
 * @Author lujichao
 * @Date 2019/2/20
 */
public interface DemoInfoRepository extends CrudRepository<DemoInfo, Long> {

}
