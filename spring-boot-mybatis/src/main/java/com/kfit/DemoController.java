package com.kfit;

import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description
 * @Author lujichao
 * @Date 2019/2/13
 */
@RestController
public class DemoController {
    @Autowired
    private DemoService demoService;

    @RequestMapping("/likeName")
    public List<Demo> likeName(String name, int pageNum, int pageSize) {
        /**
         * pageNum:第几页
         * pageSize:每页的条数
         */
        PageHelper.startPage(pageNum, pageSize);
        return demoService.likeName(name);
    }

    @RequestMapping("/save")
    public Demo save() {
        Demo demo = new Demo();
        demo.setName("李四");
        demoService.save(demo);
        return demo;
    }
}
