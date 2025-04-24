package com.ybb.feign;

import com.ybb.dto.DriverUser;
import com.ybb.dto.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("server-driver-user")
public interface DriverServiceFeign {
    // 司机端数据插入方法
    @PostMapping("/user/insert")
    ResponseResult insertDriverUser(@RequestBody DriverUser driverUser);

    @PostMapping("/user/update")
     ResponseResult updateDriverUser(@RequestBody DriverUser driverUser);

}
