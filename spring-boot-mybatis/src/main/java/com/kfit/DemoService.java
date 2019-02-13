package com.kfit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description
 * @Author lujichao
 * @Date 2019/2/13
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
public class DemoService {

    @Autowired
    private DemoMapper demoMapper;

    List<Demo> likeName(String name) {
        return demoMapper.likeName(name);
    }

    @Transactional
    void save(Demo demo) {
        demoMapper.save(demo);
    }
}
