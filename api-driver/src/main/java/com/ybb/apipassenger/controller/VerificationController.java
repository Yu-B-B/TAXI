package com.ybb.apipassenger.controller;


import com.ybb.dto.ResponseResult;
import com.ybb.request.VerificationCodeDto;
import com.ybb.apipassenger.service.VerificationCodeService;
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

    /**
     * 点击登录校验验证码
     *
     * @param verificationCodeDTO
     * @return
     */
    @PostMapping("/verification-code-check")
    public ResponseResult checkVerificationCode(@RequestBody VerificationCodeDto verificationCodeDTO){
        String driverPhone = verificationCodeDTO.getDriverPhone();
        String verificationCode = verificationCodeDTO.getVerificationCode();

        System.out.println("手机号"+driverPhone+",验证码："+verificationCode);

        return verificationCodeService.checkCode(driverPhone,verificationCode);
    }
}
