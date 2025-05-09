package com.ybb.apipassenger.controller;

import com.ybb.dto.DriverUser;
import com.ybb.dto.ResponseResult;
import com.ybb.apipassenger.service.DriverUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DriverUserController {
    @Autowired
    private DriverUserService driverUserService;

    /**
     * boss端做司机信息录入
     * @param driverUser
     * @return
     */
    @PostMapping("/driver-user/insert")
    public ResponseResult insertDriverUser(@RequestBody DriverUser driverUser){
        return driverUserService.insertDriverUser(driverUser);
    }

    /**
     * boss端做司机信息修改
     * @param driverUser
     * @return
     */
    @PostMapping("/driver-user/update")
    public ResponseResult updateDriverUser(@RequestBody DriverUser driverUser){
        return driverUserService.updateDriverUser(driverUser);
    }
}
