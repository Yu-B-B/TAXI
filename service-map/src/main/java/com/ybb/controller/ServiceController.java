package com.ybb.controller;

import com.ybb.dto.ResponseResult;
import com.ybb.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 1.创建服务，一个服务对应多个终端，一个终端对应一辆车
 * 创建服务后得到【sid】
 * 根据sid创建终端，得到【tid】
 */
@RestController
@RequestMapping("/service")
public class ServiceController {
    @Autowired
    private ServiceService service;

    @PostMapping("/create")
    public ResponseResult createService(String name){
        return service.add(name);
    }
}
