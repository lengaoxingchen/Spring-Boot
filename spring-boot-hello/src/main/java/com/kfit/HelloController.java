package com.kfit;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @Description
 * @Author lujichao
 * @Date 2019/1/31
 */
@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }

    @RequestMapping("/getDemo")
    public Demo getDemo() {
        Demo demo = new Demo();
        demo.setId(1);
        demo.setName("张三");
        demo.setCreateTime(new Date());
        demo.setRemarks("设置备注信息");
        return demo;
    }
}
