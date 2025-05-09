package com.ybb.apipassenger.controller;

import com.ybb.dto.ResponseResult;
import com.ybb.request.VerificationCodeDto;
import com.ybb.apipassenger.service.PassengerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PassengerUserController
{
    @Autowired
    private PassengerUserService passengerUserService;
    @PostMapping("/user")
    public ResponseResult loginOrRegister(@RequestBody VerificationCodeDto request){
        return ResponseResult.success(passengerUserService.loginOrRegister(request.getPhone()));
    }

//    @GetMapping("/user/") // 使用 GET 方式接收 RequestBody,该方法将会被转为Post
//    public ResponseResult getUserInfoByPhone(@RequestBody VerificationCodeDto request){
    @GetMapping("/user/{phone}")
    public ResponseResult getUserInfoByPhone(@PathVariable String phone){
        return passengerUserService.getUserInfoByPhone(phone);
    }
}
