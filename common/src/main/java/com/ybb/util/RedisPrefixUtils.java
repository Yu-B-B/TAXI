package com.ybb.util;

public class RedisPrefixUtils {
    private static String verificationCodePrefix = "passenger-verification-code-";
    private static String tokenPrefix = "token-";
    public static String blackDeviceCodePrefix = "black-device-";


    public static String getRedisKeyByPhone(String phone) {
        return verificationCodePrefix + phone;
    }

    // v1-根据手机号与身份标识生成token
    public static String generateTokenKey(String phone,String identify) {
        return tokenPrefix + phone + "-" + identify;
    }

    // v2-增加令牌类型
    public static String generateTokenKeyV2(String phone, String identify, String tokenType) {
        return tokenPrefix + phone + "-" + identify + "-" + tokenType;
    }
}
