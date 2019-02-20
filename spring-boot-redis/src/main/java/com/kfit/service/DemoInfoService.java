package com.kfit.service;

import com.kfit.bean.DemoInfo;

/**
 * @Description 服务接口
 * @Author lujichao
 * @Date 2019/2/20
 */
public interface DemoInfoService {
    DemoInfo findById(long id);
    void deleteFromCache(long id);
    void test();
}
