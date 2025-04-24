package com.ybb.controller;

import com.ybb.dto.ResponseResult;
import com.ybb.response.NumberCodeResponse;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NumberCodeController {
    /**
     * size: 后移位数
     */
    @GetMapping("/numberCode/{size}")
    public ResponseResult numberCode(@PathVariable int size) {
        System.out.println("size: " + size);

        // 生成验证码
        double mathRandom = (Math.random() * 9 + 1) * (Math.pow(10, size - 1));
        int code = (int) mathRandom;
        // 封装为对象
        /*JSONObject result = new JSONObject();
        result.put("code", 1);
        result.put("message", "success");
        JSONObject data = new JSONObject();

        data.put("numberCode", code);
        result.put("data", data);*/

        NumberCodeResponse numberCodeResponse = new NumberCodeResponse();
        numberCodeResponse.setNumberCode(code);

        return ResponseResult.success(numberCodeResponse);
    }


}
