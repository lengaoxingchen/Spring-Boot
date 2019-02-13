package com.kfit.demo.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @Description cat实体类
 * <p>
 * 使用@Entity进行实体类的持久化操作,当Jpa检测到我们的实体类当中有
 * @Entity 注解的时候, 会在数据库中自动生成对应的表结构信息
 * @Author lujichao
 * @Date 2019/1/31
 */
@Entity
public class Cat {

    @Id //指定主键
    @GeneratedValue(strategy = GenerationType.AUTO)  //自增序列
    private int id;
    private String catName;
    private int catAge;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public int getCatAge() {
        return catAge;
    }

    public void setCatAge(int catAge) {
        this.catAge = catAge;
    }
}
