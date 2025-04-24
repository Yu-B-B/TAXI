package com.ybb.request;

import lombok.Data;

@Data
public class VerificationCodeDto {
    private String phone; // 乘客手机号
    private String verificationCode; // 验证码
    private String driverPhone; // 司机手机号
}
