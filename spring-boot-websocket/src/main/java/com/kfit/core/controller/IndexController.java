package com.kfit.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description
 * @Author lujichao
 * @Date 2019/2/15
 */
@Controller
public class IndexController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }
}
