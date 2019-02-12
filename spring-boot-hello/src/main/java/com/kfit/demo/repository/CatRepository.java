package com.kfit.demo.repository;

import com.kfit.demo.bean.Cat;
import org.springframework.data.repository.CrudRepository;

/**
 * @Description
 * @Author lujichao
 * @Date 2019/1/31
 */
public interface CatRepository extends CrudRepository<Cat,Integer> {
}
