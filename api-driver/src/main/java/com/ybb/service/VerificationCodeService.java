package com.ybb.service;

import com.ybb.constant.CommonStateEnum;
import com.ybb.constant.DriverCarConstants;
import com.ybb.constant.IdentifyConstant;
import com.ybb.dto.ResponseResult;
import com.ybb.feign.DriverUserFeignClient;
import com.ybb.feign.VerificationCodeFeignClient;
import com.ybb.response.DriverUserExistsResponse;
import com.ybb.response.NumberCodeResponse;
import com.ybb.util.RedisPrefixUtils;
import lombok.extern.slf4j.Slf4j;
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
}
