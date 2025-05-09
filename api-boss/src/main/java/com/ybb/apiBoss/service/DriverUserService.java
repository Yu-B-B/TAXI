package com.ybb.apiBoss.service;

import com.ybb.dto.DriverUser;
import com.ybb.dto.ResponseResult;
import com.ybb.apiBoss.feign.DriverServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DriverUserService {
    @Autowired
    private DriverServiceFeign driverServiceFeign;

    public ResponseResult insertDriverUser(DriverUser driverUser) {
        return driverServiceFeign.insertDriverUser(driverUser);
    }

    public ResponseResult updateDriverUser(DriverUser driverUser){
        return driverServiceFeign.updateDriverUser(driverUser);
    }
}
