package com.ybb.apiDriver.feign;

import com.ybb.dto.*;
import com.ybb.response.DriverUserExistsResponse;
import com.ybb.response.OrderDriverResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("server-driver-user")
public interface DriverUserFeignClient {
    @PostMapping("/user/update")
    ResponseResult updateUser(@RequestBody DriverUser driverUser);

    @GetMapping("/driver-user/{checkPhone}")
    ResponseResult<DriverUserExistsResponse> checkDriver(@PathVariable("checkPhone") String checkPhone);

    /**
     * 根据车牌号查询司机与车辆信息
     * @param carId 车辆ID
     * @return
     */
    @GetMapping("/get-available-driver/{carId}")
    ResponseResult<OrderDriverResponse> getAvailableDriver(@PathVariable("carId") Long carId);
    /**
     * 获取车辆信息
     * @param carId 车辆id
     * @return
     */
    @GetMapping("/service/car/getCarInfo")
    ResponseResult<Car> getCarById(@RequestParam Long carId);

    @PostMapping("/driver-user-work-status/change-status")
    ResponseResult changeWorkStatus(@RequestBody DriverUserWorkStatus driverUserWorkStatus);

    @GetMapping("/driver-car-binding-relationship")
    ResponseResult<DriverCarBindingRelationship> getDriverCarRelationShip(@RequestParam String driverPhone);

    @GetMapping("/work-status")
    ResponseResult<DriverUserWorkStatus> getWorkStatus(@RequestParam Long driverId);
}
