package com.kfit.demo.repository;

import com.kfit.demo.bean.Cat;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;


/**
 * @Description
 * @Author lujichao
 * @Date 2019/2/1
 */
public interface Cat2Repository extends Repository<Cat, Integer> {

    /**
     * 1.查询方法以get|find|read开头
     * 2.设计查询条件时,条件的属性用关键字连接,要注意的是条件属性以首字母大写
     */

    Cat findByCatName(String catName);
}
