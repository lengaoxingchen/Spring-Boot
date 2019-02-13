package com.kfit.base.scheduling;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @Description 如何使用定时任务
 * @Author lujichao
 * @Date 2019/2/13
 */
@Configuration
@EnableScheduling
public class SchedulingConfig {

    @Scheduled(cron = "0/20 * * * *?") //每20秒执行一次
    public void scheduling() {
        System.out.println(">>>>>>>>> SchedulingConfig.scheduler()");
    }
}
