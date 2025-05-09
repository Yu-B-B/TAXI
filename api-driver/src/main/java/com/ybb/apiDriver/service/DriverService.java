package com.ybb.apiDriver.service;

import com.ybb.dto.DriverUser;
import com.ybb.dto.ResponseResult;
import com.ybb.apiDriver.feign.DriverUserFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DriverService {
    @Autowired
    private DriverUserFeignClient driverUserFeignClient;

    public ResponseResult updateUser(DriverUser driverUser) {
        return driverUserFeignClient.updateUser(driverUser);
    }
}
