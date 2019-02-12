package com.kfit.demo.controller;

import com.kfit.demo.bean.Cat;
import com.kfit.demo.service.CatService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Description
 * @Author lujichao
 * @Date 2019/1/31
 */
@RestController
@RequestMapping("/cat")
public class CatController {
    @Resource
    private CatService service;

    @RequestMapping("/save")
    public void save() {
        Cat cat = new Cat();
        cat.setCatAge(2);
        cat.setCatName("jack");
        service.save(cat);
    }

    @RequestMapping("delete")
    public String delete() {
        service.delete(1);
        return "delete ok";
    }

    @RequestMapping("/getAll")
    public Iterable<Cat> getAll() {
        return service.getAll();
    }

    @RequestMapping("/findByCatName")
    public Cat findByCatName(String catName) {
        return service.findByCatName(catName);
    }

    @RequestMapping("/selectByCatName")
    public Cat selectByCatName(String catName) {
        return service.selectByCatName(catName);
    }
}

