package com.ybb.apipassenger.controller;

import com.ybb.dto.ResponseResult;
import com.ybb.request.ApiDriverPointRequest;
import com.ybb.apipassenger.service.PointService;
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
     * 更新司机位置
     * @param apiDriverPointRequest
     * @return
     */
    @PostMapping("/upload")
    public ResponseResult upload(@RequestBody ApiDriverPointRequest apiDriverPointRequest) {
        return pointService.upload(apiDriverPointRequest);
    }
}