package com.ybb.controller;


import com.ybb.dto.ResponseResult;
import com.ybb.request.VerificationCodeDto;
import com.ybb.service.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 司机端登陆时获取验证码
 */
@RestController
public class VerificationController {
    @Autowired
    private VerificationCodeService verificationCodeService;

    @PostMapping("/driver-user/getVerificationCode")
    public ResponseResult getVerificationCode(@RequestBody VerificationCodeDto verificationCodeDto) {
        return verificationCodeService.getVerificationCode(verificationCodeDto.getDriverPhone());
    }
}
