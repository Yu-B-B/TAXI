package com.ybb.alipay.config;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.ybb.alipay.service.AliPayContentService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
//@ConfigurationProperties(prefix = "alipay")
//@Data
public class AlipayConfig {

//    private String appId;
//
//    private String appPrivateKey;
//
//    private String publicKey;
//
//    private String notifyUrl;

    private final AliPayContentService aliPayContentService;

    @Autowired
    public AlipayConfig(AliPayContentService aliPayContentService) {
        this.aliPayContentService = aliPayContentService;
    }
    @PostConstruct
    public void init() {
        Config config = new Config();
        // 基础配置
        config.protocol = "https";
        config.gatewayHost = "openapi-sandbox.dl.alipaydev.com";
        config.signType = "RSA2";

        // 业务配置
        Map<String,String> contents = aliPayContentService.selectAll();
        config.appId = contents.get("appId");
        config.merchantPrivateKey = contents.get("appPrivateKey");
        config.alipayPublicKey = contents.get("publicKey");
        config.notifyUrl = contents.get("notifyUrl");

//        config.appId = this.appId;
//        config.merchantPrivateKey = this.appPrivateKey;
//        config.alipayPublicKey = this.publicKey;
//        config.notifyUrl = this.notifyUrl;

        Factory.setOptions(config);
        System.out.println("支付宝配置初始化完成");
    }


}
