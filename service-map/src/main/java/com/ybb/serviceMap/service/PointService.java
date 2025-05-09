package com.ybb.serviceMap.service;

import com.ybb.dto.ResponseResult;
import com.ybb.serviceMap.remote.PointClient;
import com.ybb.request.PointRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PointService {

    @Autowired
    PointClient pointClient;

    public ResponseResult upload(PointRequest pointRequest){
        return pointClient.upload(pointRequest);
    }
}
