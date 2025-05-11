package com.ybb.alipay.config;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.ybb.alipay.service.AliPayContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
public class AlipayConfig {

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

        Map<String,String> contents = aliPayContentService.selectAll();
        // 业务配置
        config.appId = contents.get("publicKey");
        config.merchantPrivateKey = contents.get("appPrivateKey");
        config.alipayPublicKey = contents.get("appId");
        config.notifyUrl = contents.get("notifyUrl");

        Factory.setOptions(config);
        System.out.println("支付宝配置初始化完成");
    }


}
