package com.kfit;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Description
 * @Author lujichao
 * @Date 2019/2/13
 */
@Mapper
public interface DemoMapper {

    //#{name}:参数占位符
    @Select("select * from demo where name = #{name}")
    List<Demo> likeName(String name);

    @Select("select * from demo where id = #{id}")
    Demo getById(long id);

    @Select("select name from demo where id = #{id}")
    String getNameById(long id);

    /**
     * 保存数据
     */
    @Insert("insert into demo(name) values(#{name})")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    //解决无法获取自增序列id的方案
    void save(Demo demo);
}
