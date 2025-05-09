package com.ybb.apiDriver.feign;

import com.ybb.dto.ResponseResult;
import com.ybb.request.PointRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("service-map")
public interface ServiceMapClient {

    @PostMapping(value = "/point/upload")
    ResponseResult upload(@RequestBody PointRequest pointRequest);

}