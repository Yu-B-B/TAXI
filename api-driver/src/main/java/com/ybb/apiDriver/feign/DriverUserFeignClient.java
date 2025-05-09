package com.ybb.apiDriver.feign;

import com.ybb.dto.*;
import com.ybb.response.DriverUserExistsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("server-driver-user")
public interface DriverUserFeignClient {
    @PostMapping("/user/update")
    ResponseResult updateUser(@RequestBody DriverUser driverUser);

    @GetMapping("/driver-user/{checkPhone}")
    ResponseResult<DriverUserExistsResponse> checkDriver(@PathVariable("checkPhone") String checkPhone);

    /**
     * 获取车辆信息
     * @param carId 车辆id
     * @return
     */
    @GetMapping("/service/car/getCarInfo")
    ResponseResult<Car> getCarById(@RequestParam Long carId);

    @PostMapping("/driver-user-work-status")
    ResponseResult changeWorkStatus(@RequestBody DriverUserWorkStatus driverUserWorkStatus);

    @GetMapping("/driver-car-binding-relationship")
    ResponseResult<DriverCarBindingRelationship> getDriverCarRelationShip(@RequestParam String driverPhone);

    @GetMapping("/work-status")
    ResponseResult<DriverUserWorkStatus> getWorkStatus(@RequestParam Long driverId);
}
