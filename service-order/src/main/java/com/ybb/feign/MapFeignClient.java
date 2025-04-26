package com.ybb.feign;

import com.ybb.dto.ResponseResult;
import com.ybb.response.TerminalResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("service-map")
public interface MapFeignClient {

    /**
     * 终端范围内查询车辆信息
     *
     * @param center
     * @param radius
     * @return
     */
    @PostMapping("/aroundsearch")
    ResponseResult<List<TerminalResponse>> aroundsearch(@RequestParam String center ,@RequestParam Integer radius);
}
