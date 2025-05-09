package com.ybb.apipassenger.feign;

import com.ybb.dto.ResponseResult;
import com.ybb.request.ForecastPriceDto;
import com.ybb.response.DirectionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("service-map")
public interface ServiceMapFeign {
    @PostMapping("/direction/drive")
    ResponseResult<DirectionResponse> directionDrive(@RequestBody ForecastPriceDto request);
}
