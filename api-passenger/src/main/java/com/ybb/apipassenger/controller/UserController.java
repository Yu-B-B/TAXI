package com.ybb.apipassenger.controller;

import com.ybb.constant.HeaderParamConstants;
import com.ybb.dto.ResponseResult;
import com.ybb.apipassenger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户信息
 */
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 获取用户信息
     * @param request
     * @return
     */
    @GetMapping("/get-user-info")
    public ResponseResult getUserInfo(HttpServletRequest request) {
        // 获取请求头中Authorization
        String authorization = request.getHeader(HeaderParamConstants.AUTHORIZATION);
        return ResponseResult.success(userService.getUserInfo(authorization));
    }
}
