package com.ybb.apipassenger.feign;

import com.ybb.dto.ResponseResult;
import com.ybb.response.TerminalResponse;
import com.ybb.response.TrackResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service-map")
public interface ServiceMapClient {

    /**
     * 创建终端，在添加车辆信息时调用
     *
     * @param name
     * @param desc
     * @return
     */
    @PostMapping("/terminal/add")
    ResponseResult<TerminalResponse> addTerminal(@RequestParam String name, @RequestParam String desc);

    /**
     * 创建轨迹
     *
     * @param tid
     * @return
     */
    @PostMapping("/track/add")
    ResponseResult<TrackResponse> addTrack(@RequestParam String tid);
}
