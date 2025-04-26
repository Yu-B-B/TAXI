package com.ybb.feign;

import com.ybb.dto.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("service-forecast-price")
public interface PriceFeignClient {
    @GetMapping("/price-rule/checkFareVersion")
    ResponseResult<Boolean> checkFareVersion(String fareType, int fareVersion);
}
