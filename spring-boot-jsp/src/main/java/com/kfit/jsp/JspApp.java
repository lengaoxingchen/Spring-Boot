package com.kfit.jsp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * @Description
 * @Author lujichao
 * @Date 2019/2/12
 */
@SpringBootApplication
public class JspApp extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(JspApp.class, args);
    }
}
