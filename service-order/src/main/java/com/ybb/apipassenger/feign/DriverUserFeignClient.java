package com.ybb.apipassenger.feign;

import com.ybb.dto.Car;
import com.ybb.dto.ResponseResult;
import com.ybb.response.OrderDriverResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/get-available-driver/{carId}")
    ResponseResult<OrderDriverResponse> getAvailableDriver(@PathVariable("carId") Long carId);

    @GetMapping("/service/car/getCarInfo")
    ResponseResult<Car> getCarInfo(@RequestParam Long carId);
}
