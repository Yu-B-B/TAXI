package com.ybb.apipassenger.controller;

import com.ybb.dto.ResponseResult;
import com.ybb.apipassenger.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 1.创建服务，一个服务对应多个终端，一个终端对应一辆车
 * 创建服务后得到【sid】
 * 根据sid创建终端，得到【tid】
 */
@RestController
@RequestMapping("/terminal/service")
public class ServiceController {
    @Autowired
    private ServiceService service;

    @PostMapping("/create")
    public ResponseResult createService(@RequestParam String name){
        return service.add(name);
    }

    @PostMapping("/delete")
    public ResponseResult deleteService(@RequestParam String sid){
        return service.delete(sid);
    }

    @GetMapping("/get")
    public ResponseResult getService(){
        return service.get();
    }

}
