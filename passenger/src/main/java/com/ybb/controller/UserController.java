package com.ybb.controller;

import com.ybb.dto.PassengerUser;
import com.ybb.dto.ResponseResult;
import com.ybb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/get-user-info")
    public ResponseResult getUserInfo(HttpServletRequest request) {
        // 获取请求头中Authorization
        String authorization = request.getHeader("Authorization");
        return ResponseResult.success(userService.getUserInfo(authorization));
    }
}
