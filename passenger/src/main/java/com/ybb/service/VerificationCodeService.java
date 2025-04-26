package com.ybb.service;

import com.ybb.constant.CommonStateEnum;
import com.ybb.constant.IdentifyConstant;
import com.ybb.constant.TokenConstant;
import com.ybb.dto.ResponseResult;
import com.ybb.feign.PassengerPublicFeign;
import com.ybb.feign.VerificationCodeFeign;
import com.ybb.request.VerificationCodeDto;
import com.ybb.response.NumberCodeResponse;
import com.ybb.response.TokenResponse;
import com.ybb.util.JwtUtils;
import com.ybb.util.RedisPrefixUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class VerificationCodeService {
    @Autowired
    private VerificationCodeFeign verificationCodeFeign;
    @Autowired
    private PassengerPublicFeign passengerPublicFeign;


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取验证码方法
     */
    public ResponseResult generateCode(String phone) {
        // 增加校验时间内是否已发送过

        // 调用远程获取验证码
        ResponseResult<NumberCodeResponse> response = verificationCodeFeign.numberCode(6);
        int code = response.getData().getNumberCode();
        System.out.println("获取验证码：" + code);

        // 存入redis中
        // key，value，过期时间
        stringRedisTemplate.opsForValue().set(RedisPrefixUtils.getRedisKeyByPhone(phone), code + "", 1, TimeUnit.MINUTES);
        System.out.println("存入redis：1212");


        // 返回
        JSONObject result = new JSONObject();
        result.put("code", 1);
        result.put("message", "success");

        return ResponseResult.success();
    }

    /**
     * 提交后校验手机号和验证码
     */
    public ResponseResult checkCode(String phone, String code) {
        // 1. redis中获取验证码
        String key = RedisPrefixUtils.getRedisKeyByPhone(phone);
        String redisCodeValue = stringRedisTemplate.opsForValue().get(key);

        // 2. 校验验证码
        if (StringUtils.isBlank(redisCodeValue)) {
            return ResponseResult.fail(CommonStateEnum.VERIFICATION_CODE_ERROR.getCode(),
                    CommonStateEnum.VERIFICATION_CODE_ERROR.getMessage());
        }
        // 去除两端空格后比较
        if (!code.trim().equals(redisCodeValue.trim())) {
            return ResponseResult.fail(CommonStateEnum.VERIFICATION_CODE_ERROR.getCode(),
                    CommonStateEnum.VERIFICATION_CODE_ERROR.getMessage());
        }

        // 3.判断用户是否已注册过
        VerificationCodeDto codeDto = new VerificationCodeDto();
        codeDto.setPhone(phone);
        passengerPublicFeign.loginOrRegister(codeDto);

        // 颁发令牌，identity因该为枚举值/常量
        String accessToken = JwtUtils.generateToken(phone, IdentifyConstant.PASSENGER, TokenConstant.ACCESS_TOKEN);
        // 增加双token，做自动续登录功能
        String refreshToken = JwtUtils.generateToken(phone, IdentifyConstant.PASSENGER, TokenConstant.FRESH_TOKEN);

        // v1 - 将token存入redis中
//        String accessTokenKey = RedisPrefixUtils.generateTokenKey(phone, IdentifyConstant.PASSENGER);

        // v2 - 增加token类型
        String accessTokenKey = RedisPrefixUtils.generateTokenKeyV2(phone, IdentifyConstant.PASSENGER, TokenConstant.ACCESS_TOKEN);
        // refreshToken比accessToken晚过期，让refreshToken有能力重新生成 accessToken与refreshToken信息
        String refreshTokenKey = RedisPrefixUtils.generateTokenKeyV2(phone, IdentifyConstant.PASSENGER, TokenConstant.FRESH_TOKEN);

        stringRedisTemplate.opsForValue().set(accessTokenKey, accessToken, 30, TimeUnit.DAYS);
        stringRedisTemplate.opsForValue().set(refreshTokenKey, accessToken, 31, TimeUnit.DAYS);

        // 做测试
//        stringRedisTemplate.opsForValue().set(accessTokenKey, accessToken, 15, TimeUnit.SECONDS);
//        stringRedisTemplate.opsForValue().set(refreshTokenKey, accessToken, 30, TimeUnit.SECONDS);

        // 保存双token
        TokenResponse response = new TokenResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);

        return new ResponseResult().success(response);
    }


}
