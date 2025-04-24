package com.ybb.controller;

import com.ybb.dto.DriverUser;
import com.ybb.dto.ResponseResult;
import com.ybb.service.DriverUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DriverUserController {
    @Autowired
    private DriverUserService driverUserService;
    /**
     * 新增司机
     * @param driverUser
     * @return
     */
    @PostMapping("/user/insert")
    public ResponseResult addUser(@RequestBody DriverUser driverUser){
        return driverUserService.addDriverUser(driverUser);
    }

    @PostMapping("/user/update")
    public ResponseResult updateUser(@RequestBody DriverUser driverUser){
        return driverUserService.updateUser(driverUser);
    }
}
