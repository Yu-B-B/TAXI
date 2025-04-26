package com.ybb.feign;

import com.ybb.dto.PriceRule;
import com.ybb.dto.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service-forecast-price")
public interface PriceFeignClient {
    @GetMapping("/price-rule/checkFareVersion")
    ResponseResult<Boolean> checkFareVersion(String fareType, int fareVersion);

    /**
     * 判断对应城市是否有计价规则
     * @param priceRule
     * @return
     */
    @PostMapping("/price-rule/checkFareRule")
    public ResponseResult<Boolean> checkFareRule(@RequestParam PriceRule priceRule);
}
