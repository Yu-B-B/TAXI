package com.ybb.apiDriver.feign;

import com.ybb.dto.ResponseResult;
import com.ybb.response.NumberCodeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("verification-service")
public interface VerificationCodeFeignClient {
    /**
     * 调用获取验证码
     */
    @GetMapping("/numberCode/{size}")
    ResponseResult<NumberCodeResponse> numberCode(@PathVariable("size") int size);

}
