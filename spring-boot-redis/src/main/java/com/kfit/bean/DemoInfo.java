package com.kfit.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @Description
 * @Author lujichao
 * @Date 2019/2/20
 */
@Entity
public class DemoInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id@GeneratedValue
    private long id;

    private String name;

    private String pwd;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Override
    public String toString() {
        return "DemoInfo[id=" + id +
                ", name=" + name + ", pwd=" + pwd +"]";
    }
}
