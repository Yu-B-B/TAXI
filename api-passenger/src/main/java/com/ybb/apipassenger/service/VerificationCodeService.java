package com.ybb.apipassenger.service;

import com.ybb.constant.CommonStateEnum;
import com.ybb.constant.IdentifyConstant;
import com.ybb.constant.TokenConstant;
import com.ybb.dto.ResponseResult;
import com.ybb.apipassenger.feign.PassengerPublicFeign;
import com.ybb.apipassenger.feign.VerificationCodeFeign;
import com.ybb.request.VerificationCodeDto;
import com.ybb.response.NumberCodeResponse;
import com.ybb.response.TokenResponse;
import com.ybb.util.JwtUtils;
import com.ybb.util.RedisPrefixUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
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



        return ResponseResult.success(code);
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

        // 增加远程调用异常捕获
        try {
            passengerPublicFeign.loginOrRegister(codeDto);
        } catch (Exception e) {
            return ResponseResult.fail(CommonStateEnum.CALL_USER_ADD_ERROR.getCode(), CommonStateEnum.CALL_USER_ADD_ERROR.getMessage());
        }


        // 颁发令牌，identity因该为枚举值/常量
        String accessToken = JwtUtils.generateToken(phone, IdentifyConstant.PASSENGER, TokenConstant.ACCESS_TOKEN);
        // 增加双token，做自动续登录功能
        String refreshToken = JwtUtils.generateToken(phone, IdentifyConstant.PASSENGER, TokenConstant.FRESH_TOKEN);
        // 开启redis事务支持
        stringRedisTemplate.setEnableTransactionSupport(true);
        SessionCallback<Boolean> callback = new SessionCallback<Boolean>() {

            @Override
            public <K, V> Boolean execute(RedisOperations<K, V> operations) throws DataAccessException {
                // 事务结束
                stringRedisTemplate.multi();

                try {


                    // v1 - 将token存入redis中
                    // String accessTokenKey = RedisPrefixUtils.generateTokenKey(phone, IdentifyConstant.PASSENGER);

                    // v2 - 增加token类型
                    String accessTokenKey = RedisPrefixUtils.generateTokenKeyV2(phone, IdentifyConstant.PASSENGER, TokenConstant.ACCESS_TOKEN);
                    // refreshToken比accessToken晚过期，让refreshToken有能力重新生成 accessToken与refreshToken信息
                    String refreshTokenKey = RedisPrefixUtils.generateTokenKeyV2(phone, IdentifyConstant.PASSENGER, TokenConstant.FRESH_TOKEN);

                    stringRedisTemplate.opsForValue().set(accessTokenKey, accessToken, 30, TimeUnit.DAYS);
//                    int i  = 10/0;
                    stringRedisTemplate.opsForValue().set(refreshTokenKey, accessToken, 31, TimeUnit.DAYS);

                    operations.exec();

                    return true;
                } catch (Exception e) {
                    operations.discard();
                    throw new RuntimeException(e);
                }
            }
        };
        Boolean execute = stringRedisTemplate.execute(callback);
        if (execute) {
            // 保存双token
            TokenResponse response = new TokenResponse();
            response.setAccessToken(accessToken);
            response.setRefreshToken(refreshToken);

            return ResponseResult.success(response);
        }else{
            return ResponseResult.fail(CommonStateEnum.CHECK_CODE_ERROR.getCode(),CommonStateEnum.CHECK_CODE_ERROR.getMessage());
        }

        // 做测试
//        stringRedisTemplate.opsForValue().set(accessTokenKey, accessToken, 15, TimeUnit.SECONDS);
//        stringRedisTemplate.opsForValue().set(refreshTokenKey, accessToken, 30, TimeUnit.SECONDS);


    }


}
