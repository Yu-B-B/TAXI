package com.ybb.serviceorder.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RenewRedisLock {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Async
    public void renewLock(String key, String value, int time) {
        String s = redisTemplate.opsForValue().get(key);
        if (StringUtils.isNotBlank(value) && value.equals(s)) {
            int period = time / 3;
            System.out.println("开始等待判断");
            try {
                Thread.sleep(period);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("开始续期");
            redisTemplate.expire(key, period, TimeUnit.SECONDS);
        } else {
            return;
        }
        renewLock(key, value, time);
    }
}
