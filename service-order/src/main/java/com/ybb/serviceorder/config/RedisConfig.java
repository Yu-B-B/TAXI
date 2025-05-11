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
        return Redisson.create(config);
    }

    // 主从模式
    @Bean("redissonMSYml")
    public RedissonClient redisson2() {
        Config config = null;
        try {
            config = Config.fromYAML(new ClassPathResource("/redisson-config/master-replicaof.yaml").getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Redisson.create(config);
    }

    // 哨兵模式
    @Bean("redissonSentinelYml")
    public RedissonClient redisson3() {
        Config config = null;
        try {
            config = Config.fromYAML(new ClassPathResource("/redisson-config/sentinel.yaml").getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Redisson.create(config);
    }
    // 集群模式
    @Bean("redissonClusterYml")
    public RedissonClient redisson4() {
        Config config = null;
        try {
            config = Config.fromYAML(new ClassPathResource("/redisson-config/cluster.yaml").getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Redisson.create(config);
    }

    // 红锁

    @Bean("redissonClient1")
    public RedissonClient redissonClient1(){
//        Config config = new Config();
//        config.useSingleServer().setAddress("redis://127.0.0.1:6379").setDatabase(0);
//
//        return Redisson.create(config);
        // 便于测试
        return Redisson.create();
    }
    @Bean("redissonClient2")
    public RedissonClient redissonClient2(){
//        Config config = new Config();
//        config.useSingleServer().setAddress("redis://127.0.0.1:6380").setDatabase(0);
//
//        return Redisson.create(config);
        // 便于测试
        return Redisson.create();
    }

    @Bean("redissonClient3")
    public RedissonClient redissonClient3(){
//        Config config = new Config();
//        config.useSingleServer().setAddress("redis://127.0.0.1:6381").setDatabase(0);
//
//        return Redisson.create(config);
        // 便于测试
        return Redisson.create();
    }

    @Bean("redissonClient4")
    public RedissonClient redissonClient4(){
//        Config config = new Config();
//        config.useSingleServer().setAddress("redis://127.0.0.1:6382").setDatabase(0);
//
//        return Redisson.create(config);
        // 便于测试
        return Redisson.create();
    }
    @Bean("redissonClient5")
    public RedissonClient redissonClient5(){
//        Config config = new Config();
//        config.useSingleServer().setAddress("redis://127.0.0.1:6383").setDatabase(0);
//
//        return Redisson.create(config);
        // 便于测试
        return Redisson.create();
    }

}
