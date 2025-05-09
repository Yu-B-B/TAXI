package com.ybb.apipassenger.service;

import com.ybb.dto.ResponseResult;
import com.ybb.remote.MapDirectionClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DirectionService {
    @Autowired
    private MapDirectionClient mapDirectionClient;

    /*
     * 调用高德地图获取经纬度之间的距离
     * */
    public ResponseResult driving(String depLatitude, String depLongitude, String destLongitude, String destLatitude) {

        // 获取地图中基础数据

        return ResponseResult.success(mapDirectionClient.directionData(depLongitude, depLatitude, destLongitude, destLatitude));
    }
}
