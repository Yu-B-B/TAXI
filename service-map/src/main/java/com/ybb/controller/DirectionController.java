package com.ybb.controller;

import com.ybb.dto.ResponseResult;
import com.ybb.request.ForecastPriceDto;
import com.ybb.service.DirectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DirectionController {
    @Autowired
    private DirectionService directionService;

    @PostMapping("/direction/drive")
    public ResponseResult directionDrive(@RequestBody ForecastPriceDto request) {
       return directionService.driving(request.getDepLongitude(),request.getDepLatitude(),request.getDestLongitude(),request.getDestLatitude());
    }
}
