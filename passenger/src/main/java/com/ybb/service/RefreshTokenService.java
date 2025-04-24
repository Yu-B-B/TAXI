package com.ybb.service;

import com.ybb.constant.CommonStateEnum;
import com.ybb.constant.TokenConstant;
import com.ybb.dto.ResponseResult;
import com.ybb.dto.TokenResult;
import com.ybb.response.TokenResponse;
import com.ybb.util.JwtUtils;
import com.ybb.util.RedisPrefixUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RefreshTokenService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public ResponseResult refreshToken(String refreshToken) {
        // 1、解析refreshToken获取【手机号】、【身份标识】
        TokenResult tokenResult = JwtUtils.checkToken(refreshToken);
        if (tokenResult == null) {
            return ResponseResult.fail(CommonStateEnum.TOKEN_ERROR.getCode(), CommonStateEnum.TOKEN_ERROR.getMessage());
        }

        String phone = tokenResult.getPhone();
        String identity = tokenResult.getIdentity();

        // 2、通过【手机号】、【身份标识】重新加密
//        String refreshTokenKey = JwtUtils.generateToken(phone, identity, TokenConstant.FRESH_TOKEN);
        String refreshTokenKey = RedisPrefixUtils.generateTokenKeyV2(phone, identity, TokenConstant.FRESH_TOKEN);

        // 3、对比重新加密的refreshToken 与 【参数refreshToken】
        String refreshTokenValueFromRedis = stringRedisTemplate.opsForValue().get(refreshTokenKey);
        if ((StringUtils.isBlank(refreshTokenValueFromRedis)) || !refreshTokenValueFromRedis.equals(refreshToken)) {
            return ResponseResult.fail(CommonStateEnum.TOKEN_ERROR.getCode(), CommonStateEnum.TOKEN_ERROR.getMessage());
        }

        // 4、重新生成【refreshToken】 与 【accessToken】
        String accessTokenKey = RedisPrefixUtils.generateTokenKeyV2(phone, identity, TokenConstant.ACCESS_TOKEN);
        String accessTokenValue = JwtUtils.generateToken(phone, identity, TokenConstant.ACCESS_TOKEN);

        String refreshTokenValue = JwtUtils.generateToken(phone, identity, TokenConstant.FRESH_TOKEN);

        // 5、重新保存到redis中
        stringRedisTemplate.opsForValue().set(accessTokenKey, accessTokenValue, 30, TimeUnit.DAYS);
        stringRedisTemplate.opsForValue().set(refreshTokenKey, refreshTokenValue, 31, TimeUnit.DAYS);

        // 做测试
//        stringRedisTemplate.opsForValue().set(accessTokenKey, accessTokenValue, 15, TimeUnit.SECONDS);
//        stringRedisTemplate.opsForValue().set(refreshTokenKey, refreshTokenValue, 30, TimeUnit.SECONDS);

        TokenResponse result = new TokenResponse();
        result.setAccessToken(accessTokenValue);
        result.setRefreshToken(refreshTokenValue);
        return ResponseResult.success(result);
    }
}
