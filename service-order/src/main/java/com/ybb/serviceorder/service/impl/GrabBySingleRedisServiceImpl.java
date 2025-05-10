package com.ybb.serviceorder.service.impl;

import com.ybb.dto.ResponseResult;
import com.ybb.request.DriverGrabRequest;
import com.ybb.serviceorder.service.GrabService;
import com.ybb.serviceorder.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("grabBySingleService")
@Slf4j
public class GrabBySingleRedisServiceImpl implements GrabService {

    @Autowired
    private OrderService orderService;

    @Override
    public ResponseResult orderGrab(DriverGrabRequest request) {
        log.info("开始锁 single redis lock start");
        ResponseResult result = orderService.orderGrab(request);
        log.info("结束锁 single redis get off");
        return result;
    }
}
