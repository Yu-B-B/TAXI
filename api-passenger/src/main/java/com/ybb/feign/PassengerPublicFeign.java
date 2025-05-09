package com.ybb.feign;

import com.ybb.dto.PassengerUser;
import com.ybb.dto.ResponseResult;
import com.ybb.request.VerificationCodeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("passenger-public-service")
public interface PassengerPublicFeign {
    @PostMapping("/user")
    ResponseResult loginOrRegister(@RequestBody VerificationCodeDto request);

//    @GetMapping("/user/") // 使用 GET 方式接收 RequestBody,该方法将会被转为Post
//    ResponseResult getUserInfoByPhone(@RequestBody VerificationCodeDto request);

    @GetMapping("/user/{phone}")
    ResponseResult<PassengerUser> getUserInfoByPhone(@PathVariable String phone);
}
