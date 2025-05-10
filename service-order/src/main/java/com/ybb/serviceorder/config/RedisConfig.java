package com.ybb.serviceorder.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RedisConfig {
    private String potocol = "redis://";

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;

    @Bean("redissonBase")
    public RedissonClient redisson() {
        Config config = new Config();
        config.useSingleServer().setAddress(potocol + host + ":" + port).setDatabase(0);
        return Redisson.create(config);
    }

    @Bean("redissonSingleYml")
    public RedissonClient redisson1() {
        Config config = null;
        try {
            config = Config.fromYAML(new ClassPathResource("/redisson-config/single-server.yaml").getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        config.useSingleServer().setAddress(potocol + host + ":" + port).setDatabase(0);
        return Redisson.create(config);
    }


}
