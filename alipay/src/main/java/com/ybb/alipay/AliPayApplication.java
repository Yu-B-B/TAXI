package com.ybb.alipay;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableFeignClients
@MapperScan("com.ybb.alipay.mapper")
public class AliPayApplication {
    public static void main(String[] args) {
        SpringApplication.run(AliPayApplication.class, args);
    }

    @GetMapping("/test")
    public String test() {
        return "支付宝回调了";
    }
}
