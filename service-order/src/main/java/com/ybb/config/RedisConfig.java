package com.ybb.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RedisConfig {
    @Bean
    public RedissonClient redisson() {
        Config config = new Config();

        return Redisson.create(config);
    }
}
