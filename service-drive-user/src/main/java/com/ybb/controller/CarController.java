package com.ybb.controller;

import com.ybb.dto.Car;
import com.ybb.dto.ResponseResult;
import com.ybb.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CarController {
    @Autowired
    private CarService carService;

    /**
     * 添加车辆信息
     * @param car
     * @return
     */
    @PostMapping("/addCar")
    public ResponseResult addCar(@RequestBody Car car) {
        return carService.addCar(car);
    }
}
