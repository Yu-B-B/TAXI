package com.ybb.apiBoss.service;

import com.ybb.apiBoss.feign.DriverServiceFeign;
import com.ybb.dto.Car;
import com.ybb.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarService {
    @Autowired
    private DriverServiceFeign driverServiceFeign;

    public ResponseResult addCar(Car car) {
        return driverServiceFeign.addCar(car);
    }
}
