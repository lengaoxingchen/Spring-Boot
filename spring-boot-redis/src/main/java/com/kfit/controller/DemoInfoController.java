package com.kfit.controller;

import com.kfit.bean.DemoInfo;
import com.kfit.service.DemoInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 测试类
 * @Author lujichao
 * @Date 2019/2/20
 */
@Slf4j
@RestController
public class DemoInfoController {

    @Autowired
    DemoInfoService demoInfoService;

    @RequestMapping("/test")
    public String test() {
        DemoInfo loaded = demoInfoService.findById(1);
        log.info("loaded =" + loaded);
        DemoInfo cached = demoInfoService.findById(1);
        log.info("cashed=" + cached);
        loaded = demoInfoService.findById(2);
        log.info("loaded2="+loaded);
        return "ok";
    }

    @RequestMapping("/delete")
    public String delete(long id){
        demoInfoService.deleteFromCache(id);
        return "ok";
    }

    @RequestMapping("/test1")
    public String test1(){
        demoInfoService.test();
        log.info("DemoInfoController.test1()");
        return "ok";
    }
}
