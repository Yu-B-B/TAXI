package com.ybb.feign;

import com.ybb.dto.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("server-driver-user")
public interface DriverUserFeignClient {
    /**
     * 判断当前城市是否有可用司机
     *
     * @param cityCode
     * @return
     */
    @GetMapping("/checkExistsUsefulDriver")
    ResponseResult<Boolean> checkExistsUsefulDriver(@RequestParam String cityCode);
}
