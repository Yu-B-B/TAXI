package com.ybb.serviceorder.service.impl;

import com.ybb.constant.CommonStateEnum;
import com.ybb.dto.ResponseResult;
import com.ybb.request.DriverGrabRequest;
import com.ybb.serviceorder.service.GrabService;
import com.ybb.serviceorder.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service("grabByRedisService")
public class GrabByRedisServiceImpl implements GrabService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public ResponseResult orderGrab(DriverGrabRequest request) {
        String orderId = request.getOrderId() + "";
        String driverId = request.getDriverId() + "";
        Boolean flag = redisTemplate.opsForValue().setIfAbsent(orderId, driverId,20, TimeUnit.SECONDS);
        ResponseResult grab = null;
        if (flag) {
            grab = orderService.orderGrab(request);
            redisTemplate.delete(orderId);
        } else {
            return ResponseResult.fail(CommonStateEnum.ORDER_GRABING.getCode(), CommonStateEnum.ORDER_GRABING.getMessage());
        }
        return grab;
    }
}
