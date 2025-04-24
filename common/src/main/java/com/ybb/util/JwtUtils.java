package com.ybb.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ybb.dto.TokenResult;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class JwtUtils {

    // salt
    private static final String SIGN = "ybb";

    // 手机号
    private static final String JWT_KEY_PHONE = "passengerPhone";
    // 身份标识
    private static final String JWT_KEY_IDENTITY = "identity";
    // token类型
    private static final String JWT_TOKEN_TYPE = "tokenTypes";
    // 增加时间
    private static final String JWT_TOKEN_TIME = "tokenTime";

    // 生成token
    public static String generateToken(String phone, String identity, String tokenType) {
        HashMap<String, String> map = new HashMap<>();
        map.put(JWT_KEY_PHONE, phone);
        map.put(JWT_KEY_IDENTITY, identity);
        map.put(JWT_TOKEN_TYPE, tokenType);



        Calendar instance = Calendar.getInstance();
        // 默认令牌过期时间24小时
        instance.add(Calendar.DATE, 1);

        // 增加时间作为生成Token条件，防止生成内容一直重复
        Date date = instance.getTime();
        map.put(JWT_TOKEN_TIME, date.toString());

        // 创建jwt builder
        JWTCreator.Builder builder = JWT.create();

        // payload
        map.forEach(
                (k, v) -> {
                    builder.withClaim(k, v);
                });

        // 指定令牌过期时间
        String token = builder
//                .withExpiresAt(instance.getTime()) // v1整合过期时间,v2通过token控制时间
                .sign(Algorithm.HMAC256(SIGN)); // 生成token
        return token;
    }

    // 解析token
    public static TokenResult resolveToken(String token) {
        DecodedJWT verify = JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token);
        String phone = verify.getClaim(JWT_KEY_PHONE).asString();
        String identity = verify.getClaim(JWT_KEY_IDENTITY).asString();

        TokenResult result = new TokenResult();
        result.setPhone(phone);
        result.setIdentity(identity);
        return result;
    }

    /**
     * 校验token是否有效
     * */
    public static TokenResult checkToken(String token) {
        TokenResult tokenResult = new TokenResult();
        try {
            tokenResult = JwtUtils.resolveToken(token);
        } catch (Exception e) {
        }
        return tokenResult;
    }

}
