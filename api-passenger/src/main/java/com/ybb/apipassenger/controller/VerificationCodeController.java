package com.ybb.apipassenger.controller;

import com.ybb.dto.ResponseResult;
import com.ybb.request.VerificationCodeDto;
import com.ybb.apipassenger.service.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 乘客登录时 【获取验证码】 与 【验证码校验】
 */
@RestController
@RequestMapping("/api/passenger")
public class VerificationCodeController {
    @Autowired
    private VerificationCodeService verificationCodeService;

    /**
     * 获取验证码
     * @param verificationCodeDto
     * @return
     */
    @GetMapping("/verification-code")
    public ResponseResult verificationCode(@RequestBody VerificationCodeDto verificationCodeDto) {
        System.out.println("收到内容手机号为：" + verificationCodeDto.getPhone());
        return verificationCodeService.generateCode(verificationCodeDto.getPhone());
    }

    /**
     * 校验验证码
     */
    @GetMapping("/verification-code-check")
    public ResponseResult checkCode(@RequestBody VerificationCodeDto verificationCodeDto) {
        String phone = verificationCodeDto.getPhone();
        String code = verificationCodeDto.getVerificationCode();
        return verificationCodeService.checkCode(phone, code);
    }

}
