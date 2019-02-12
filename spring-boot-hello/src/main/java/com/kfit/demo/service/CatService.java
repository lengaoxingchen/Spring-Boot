package com.kfit.demo.service;

import com.kfit.demo.bean.Cat;
import com.kfit.demo.dao.CatDao;
import com.kfit.demo.repository.Cat2Repository;
import com.kfit.demo.repository.CatRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

/**
 * @Description
 * @Author lujichao
 * @Date 2019/1/31
 */
@Service
public class CatService {
    @Resource
    private CatRepository catRepository;

    @Resource
    private Cat2Repository cat2Repository;

    @Resource
    private CatDao catDao;

    /**
     * 保存
     *
     * @param cat
     */
    @Transactional
    public void save(Cat cat) {
        catRepository.save(cat);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(int id) {
        catRepository.delete(id);
    }

    /**
     * 查询所有
     *
     * @return
     */
    public Iterable<Cat> getAll() {
        return catRepository.findAll();
    }

    public Cat findByCatName(String catName) {
        return cat2Repository.findByCatName(catName);
    }


    public Cat selectByCatName(String catName) {
        return catDao.selectByCatName(catName);
    }
}
