package com.ybb.interceptor;

import com.ybb.constant.TokenConstant;
import com.ybb.dto.ResponseResult;
import com.ybb.dto.TokenResult;
import com.ybb.util.JwtUtils;
import com.ybb.util.RedisPrefixUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class JwtInterceptor implements HandlerInterceptor {
    // 当前内容在Spring初始化Bean之前进行了初始化，所以stringRedisTemplate为空指针，交给InterceptorConfig做前置处理
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String resultString = "";
        boolean result = true;
        String token = request.getHeader("Authorization");

        TokenResult tokenResult = JwtUtils.checkToken(token);
        /*try {
            tokenResult = JwtUtils.resolveToken(token);
        } catch (SignatureVerificationException e) {
            resultString = "token sign error";
            result = false;
        } catch (TokenExpiredException e) {
            resultString = "token expired";
            result = false;
        } catch (AlgorithmMismatchException e) {
            resultString = "algorithm mismatch";
            result = false;
        } catch (Exception e) {
            resultString = "token error";
            result = false;
        }*/

        // 获取redis中token
        if (tokenResult == null) {
            resultString = "token is null";
            result = false;
        } else {
            String identity = tokenResult.getIdentity();
            String phone = tokenResult.getPhone();
            // v1-采用手机号与身份标识生成唯一token
//            String tokenKey = RedisPrefixUtils.generateTokenKey(phone, identity);
            String tokenKey = RedisPrefixUtils.generateTokenKeyV2(phone, identity, TokenConstant.ACCESS_TOKEN);
            String tokenRedis = stringRedisTemplate.opsForValue().get(tokenKey);

            // v1 - 校验 token
            /*if (StringUtils.isBlank(tokenRedis)) {
                resultString = "token invalid";
                result = false;
            } else {
                if (!tokenRedis.trim().equals(token.trim())) {
                    resultString = "token invalid";
                    result = false;
                }
            }*/
            // v2 - 优化判断
            if((StringUtils.isBlank(tokenRedis)) || !(tokenRedis.trim().equals(token.trim()))) {
                resultString = "token invalid";
                result = false;
            }
        }


        if (!result) {
            PrintWriter out = response.getWriter();
            out.print(JSONObject.fromObject(ResponseResult.fail(resultString)).toString());
        }

        return result;
    }
}
