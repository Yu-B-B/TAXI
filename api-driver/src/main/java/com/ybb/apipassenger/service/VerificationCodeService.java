package com.ybb.apipassenger.service;

import com.ybb.constant.CommonStateEnum;
import com.ybb.constant.DriverCarConstants;
import com.ybb.constant.IdentifyConstant;
import com.ybb.constant.TokenConstant;
import com.ybb.dto.ResponseResult;
import com.ybb.apipassenger.feign.DriverUserFeignClient;
import com.ybb.apipassenger.feign.VerificationCodeFeignClient;
import com.ybb.response.DriverUserExistsResponse;
import com.ybb.response.NumberCodeResponse;
import com.ybb.response.TokenResponse;
import com.ybb.util.JwtUtils;
import com.ybb.util.RedisPrefixUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class VerificationCodeService {
    @Autowired
    private DriverUserFeignClient driverUserFeignClient;
    @Autowired
    private VerificationCodeFeignClient verificationCodeFeignClient;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public ResponseResult getVerificationCode(String driverPhone) {
        // 查看用户是否存在
        ResponseResult<DriverUserExistsResponse> checkResult = driverUserFeignClient.checkDriver(driverPhone);
        DriverUserExistsResponse data = checkResult.getData();
        int ifExists = data.getIfExists();
        if (ifExists == DriverCarConstants.DRIVER_NOT_EXISTS) {
            return ResponseResult.fail(CommonStateEnum.DRIVER_NOT_EXITST.getCode(), CommonStateEnum.DRIVER_NOT_EXITST.getMessage());
        }
        log.info(driverPhone + " 的司机存在");

        // 获取验证码
        ResponseResult<NumberCodeResponse> codeResult = verificationCodeFeignClient.numberCode(6);
        NumberCodeResponse codeData = codeResult.getData();
        int verificationCode = codeData.getNumberCode();

        // 使用三方服务发送验证码

        // 将验证码存入redis
        String redisKey = RedisPrefixUtils.generateTokenKey(driverPhone, IdentifyConstant.DRIVER);
        stringRedisTemplate.opsForValue().set(redisKey, verificationCode + "", 2, TimeUnit.MINUTES);

        return ResponseResult.success("");
    }

    /**
     * 司机登录校验验证码
     *
     * @param driverPhone
     * @param verificationCode
     * @return
     */
    public ResponseResult checkCode(String driverPhone, String verificationCode) {
        // 根据手机号，去redis读取验证码
        // 生成key
        String key = RedisPrefixUtils.generateTokenKey(driverPhone, IdentifyConstant.DRIVER);
        // 根据key获取value
        String codeRedis = stringRedisTemplate.opsForValue().get(key);
        System.out.println("redis中的value：" + codeRedis);

        // 校验验证码
        if (StringUtils.isBlank(codeRedis)) {
            return ResponseResult.fail(CommonStateEnum.VERIFICATION_CODE_ERROR.getCode(), CommonStateEnum.VERIFICATION_CODE_ERROR.getMessage());
        }
        if (!verificationCode.trim().equals(codeRedis.trim())) {
            return ResponseResult.fail(CommonStateEnum.VERIFICATION_CODE_ERROR.getCode(), CommonStateEnum.VERIFICATION_CODE_ERROR.getMessage());
        }

        // 颁发令牌，不应该用魔法值，用常量
        String accessToken = JwtUtils.generateToken(driverPhone, IdentifyConstant.DRIVER, TokenConstant.ACCESS_TOKEN);
        String refreshToken = JwtUtils.generateToken(driverPhone, IdentifyConstant.DRIVER, TokenConstant.FRESH_TOKEN);

        // 将token存到redis当中
        String accessTokenKey = RedisPrefixUtils.generateTokenKeyV2(driverPhone, IdentifyConstant.DRIVER, TokenConstant.ACCESS_TOKEN);
        String refreshTokenKey = RedisPrefixUtils.generateTokenKeyV2(driverPhone, IdentifyConstant.DRIVER, TokenConstant.FRESH_TOKEN);

        stringRedisTemplate.opsForValue().set(accessTokenKey, accessToken, 30, TimeUnit.DAYS);
        stringRedisTemplate.opsForValue().set(refreshTokenKey, refreshToken, 31, TimeUnit.DAYS);

        // 响应
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccessToken(accessToken);
        tokenResponse.setRefreshToken(refreshToken);
        return ResponseResult.success(tokenResponse);
    }
}
