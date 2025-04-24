package com.ybb.feign;

import com.ybb.dto.ResponseResult;
import com.ybb.request.ForecastPriceDto;
import com.ybb.response.ForecastPriceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;

@FeignClient("service-forecast-price")
public interface ServicePriceClient {
    @PostMapping("/forecast-price")
    ResponseResult<ForecastPriceResponse> forecastPrice(@RequestBody ForecastPriceDto request);
}

