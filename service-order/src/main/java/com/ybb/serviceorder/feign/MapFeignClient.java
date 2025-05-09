package com.ybb.serviceorder.feign;

import com.ybb.dto.ResponseResult;
import com.ybb.response.TerminalResponse;
import com.ybb.response.TrsearchResponse;
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
    @PostMapping("/terminal/aroundsearch")
    ResponseResult<List<TerminalResponse>> aroundsearch(@RequestParam String center, @RequestParam Integer radius);

    /**
     * 轨迹查询
     *
     * @param tid
     * @param starttime
     * @param endtime
     * @return
     */
    @PostMapping("/track/trsearch")
    ResponseResult<TrsearchResponse> trsearch(@RequestParam String tid, @RequestParam Long starttime, @RequestParam Long endtime);
}
