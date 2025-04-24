package com.ybb.feign;

import com.ybb.dto.DriverUser;
import com.ybb.dto.ResponseResult;
import com.ybb.response.DriverUserExistsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("server-driver-user")
public interface DriverUserFeignClient {
    @PostMapping("/user/update")
    ResponseResult updateUser(@RequestBody DriverUser driverUser);

    @GetMapping("/driver-user/{checkPhone}")
    ResponseResult<DriverUserExistsResponse> checkDriver(@PathVariable("checkPhone") String checkPhone);
}
