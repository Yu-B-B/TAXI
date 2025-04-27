package com.ybb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class DriverSeeApplication {
    public static void main(String[] args) {
        SpringApplication.run(DriverSeeApplication.class, args);
    }
}
