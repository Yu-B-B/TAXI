package com.ybb.controller;

import com.ybb.dto.ResponseResult;
import com.ybb.response.TokenResponse;
import com.ybb.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RefreshTokenController {
    @Autowired
    private RefreshTokenService tokenService;
    @PostMapping("/token-refresh")
    public ResponseResult tokenRefresh(@RequestBody TokenResponse tokenResponse) {
        tokenService.refreshToken(tokenResponse.getRefreshToken());
        return ResponseResult.success();
    }
}