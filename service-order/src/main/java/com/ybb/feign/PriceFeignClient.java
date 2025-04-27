package com.ybb.feign;

import com.ybb.dto.PriceRule;
import com.ybb.dto.ResponseResult;
import com.ybb.request.PriceRuleIsNewRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient("service-forecast-price")
public interface PriceFeignClient {
    @PostMapping("/price-rule/checkFareVersion")
    ResponseResult<Boolean> checkFareVersion(@RequestBody PriceRuleIsNewRequest request);

    /**
     * 判断对应城市是否有计价规则
     * @param priceRule
     * @return
     */
    @PostMapping("/price-rule/checkFareRule")
    ResponseResult<Boolean> checkFareRule(@RequestParam PriceRule priceRule);

    @PostMapping("/calculate-price")
    ResponseResult<BigDecimal> calculatePrice(@RequestParam Integer distance , @RequestParam Integer duration, @RequestParam String cityCode, @RequestParam String vehicleType);
}
