package com.kfit;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * 注意
 * 1.在Thymeleaf模板文件中,标签是需要闭合的,3.0之前是需要闭合的
 * 2.thymeleaf 3.0+ 是可以不强制要求闭合的
 *
 * @Description
 * @Author lujichao
 * @Date 2019/2/11
 */
@Controller
@RequestMapping("/templates")
public class TemplatesController {
    /**
     * 映射地址是:/templates/hello
     *
     * @return
     */
    @RequestMapping("/hello")
    public String hello(Map<String, Object> map) {
        map.put("name", "Andy");
        return "hello";
    }

    @RequestMapping("/helloFtl")
    public String helloFtl(Map<String, Object> map) {
        map.put("name", "Andy");
        return "helloFtl";
    }
}



