package com.kfit.jsp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @Description 测试
 * @Author lujichao
 * @Date 2019/2/12
 */
@Controller
public class HelloController {

    @Value("${application.hello:Hello Angel}")
    private String hello;

    @RequestMapping("/helloJsp")
    public String helloJsp(Map<String, Object> map) {
        System.out.println("HelloController.helloJsp().hello=" + hello);
        map.put("hello", hello);
        return "helloJsp";
    }

    @RequestMapping("/index")
    public String index(){
        return "index";
    }
}
