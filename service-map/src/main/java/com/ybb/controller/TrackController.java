package com.ybb.controller;

import com.ybb.dto.ResponseResult;
import com.ybb.service.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.RespectBinding;

/**
 * 轨迹接口，在车辆初始化时进行调用
 */
@RestController
@RequestMapping("/track")
public class TrackController {

    @Autowired
    TrackService trackService;

    /**
     * 轨迹创建
     * @param tid
     * @return
     */
    @PostMapping("/add")
    public ResponseResult add(String tid){

        return trackService.add(tid);
    }
}
