package com.kfit.service.impl;

import com.kfit.bean.DemoInfo;
import com.kfit.repository.DemoInfoRepository;
import com.kfit.service.DemoInfoService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description  数据处理类
 * @Author lujichao
 * @Date 2019/2/20
 */
@Slf4j
@Service
public class DemoInfoServiceImpl implements DemoInfoService {

    @Resource
    private DemoInfoRepository demoInfoRepository;

    @Resource
    private RedisTemplate<String,String> redisTemplate;

    //keyGenerator="myKeyGenerator"
    @Cacheable(value = "demoInfo") //缓存,这里没有指定key
    @Override
    public DemoInfo findById(long id) {
        log.info("DemoInfoServiceImpl.findById()=======从数据库中进行获取的...id="+id);
        return demoInfoRepository.findOne(id);
    }

    @Override
    @CacheEvict(value = "demoInfo")
    public void deleteFromCache(long id) {
        log.info("DemoInfoServiceImpl.delete(),从缓存中删除");
    }

    @Override
    public void test() {
        ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set("myKey4","random1="+Math.random());
        log.info(valueOperations.get("myKey4"));
    }

}
