package com.ybb.apipassenger.controller;

import com.ybb.request.PushRequest;
import com.ybb.util.SsePrefixUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DriverSeeController {
    public static Map<String, SseEmitter> sseEmitterMap = new HashMap<>();

    @GetMapping("/connect/{userId}/{identity}")
    public SseEmitter connect(@PathVariable Long userId,@PathVariable String identity) {
        SseEmitter emitter = new SseEmitter(0L);
        String sseMapKey = SsePrefixUtils.generatorSseKey(userId,identity);

        System.out.println("连接的用户信息："+userId);
        sseEmitterMap.put(sseMapKey, emitter);

        return emitter;
    }

    // 推送消息
    @PostMapping("/push")
    public String push(@RequestBody PushRequest pushRequest) {
        Long userId = pushRequest.getUserId();
        String identity = pushRequest.getIdentity();
        String content = pushRequest.getContent();

        String sseMapKey = SsePrefixUtils.generatorSseKey(userId,identity);

        try {
            if (sseEmitterMap.containsKey(sseMapKey)){
                sseEmitterMap.get(sseMapKey).send(content);
            }else {
                return "推送失败";
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "已发送消息";
    }

    @GetMapping("/close")
    public String close(@RequestParam Long userId,@RequestParam String identity) {
        String sseMapKey = SsePrefixUtils.generatorSseKey(userId,identity);

        System.out.println("关闭连接：");
        if (sseEmitterMap.containsKey(sseMapKey)){
            sseEmitterMap.remove(sseMapKey);
        }
        return "close 成功";

    }
}

