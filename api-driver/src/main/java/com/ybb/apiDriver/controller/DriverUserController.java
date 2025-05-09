package com.ybb.apiDriver.controller;

import com.ybb.dto.DriverUser;
import com.ybb.dto.ResponseResult;
import com.ybb.apiDriver.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 司机端自行调用修改个人信息
 */
@RestController
public class DriverUserController {
    @Autowired
    private DriverService driverService;

    @PostMapping("/driver-user/update")
    public ResponseResult updateUser(@RequestBody DriverUser driverUser) {
        return driverService.updateUser(driverUser);
    }
}
