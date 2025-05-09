package com.ybb.apipassenger.service;

import com.ybb.dto.PassengerUser;
import com.ybb.dto.ResponseResult;
import com.ybb.dto.TokenResult;
import com.ybb.apipassenger.feign.PassengerPublicFeign;
import com.ybb.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private PassengerPublicFeign passengerPublicFeign;

    public ResponseResult getUserInfo(String accessToken) {
        // 解析 accessToken
        TokenResult token = JwtUtils.checkToken(accessToken);
        String phone = token.getPhone();

        // 根据手机号查询【远程调用】获取用户信息
        // 不能使用Get方法接收RequestBody中内容
//        VerificationCodeDto verificationCodeDto = new VerificationCodeDto();
//        verificationCodeDto.setPhone(phone);
//        ResponseResult<PassengerUser> userInfoResult = passengerPublicFeign.getUserInfoByPhone(verificationCodeDto);

        ResponseResult<PassengerUser> userInfoResult = passengerPublicFeign.getUserInfoByPhone(phone);
        PassengerUser data = userInfoResult.getData();

        return ResponseResult.success(data);
    }
}

