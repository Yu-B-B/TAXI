package com.ybb.serviceUser;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@MapperScan("com.ybb.serviceUser.mapper")
@EnableFeignClients
@EnableDiscoveryClient
public class ServiceDriveApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceDriveApplication.class, args);
    }
}
