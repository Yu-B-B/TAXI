package com.ybb.feign;

import com.ybb.dto.DriverUser;
import com.ybb.dto.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("server-driver-user")
public interface DriverUserFeignClient {
    @PostMapping("/user/update")
    ResponseResult updateUser(@RequestBody DriverUser driverUser);
}
