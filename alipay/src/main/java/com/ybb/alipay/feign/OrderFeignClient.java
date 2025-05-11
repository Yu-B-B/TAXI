package com.ybb.alipay.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("service-order")
public interface OrderFeignClient {
}
