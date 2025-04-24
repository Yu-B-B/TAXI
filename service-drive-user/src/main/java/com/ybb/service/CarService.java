package com.ybb.service;

import com.ybb.dto.Car;
import com.ybb.dto.ResponseResult;
import com.ybb.mapper.CarMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarService {
    @Autowired
    private CarMapper carMapper;

    public ResponseResult addCar(Car car) {
        carMapper.insert(car);
        return ResponseResult.success("");
    }
}
