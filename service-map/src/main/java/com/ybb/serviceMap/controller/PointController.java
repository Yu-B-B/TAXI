package com.ybb.serviceMap.controller;

import com.ybb.dto.ResponseResult;
import com.ybb.request.PointRequest;
import com.ybb.serviceMap.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/point")
public class PointController {

    @Autowired
    private PointService pointService;

    /**
     * 位置信息上传，在司机客户端调用
     *
     * @param pointRequest
     * @return
     */
    @PostMapping("/upload")
    public ResponseResult upload(@RequestBody PointRequest pointRequest){
        return pointService.upload(pointRequest);
    }
}
