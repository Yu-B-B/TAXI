package com.ybb.serviceorder.service.impl;

import com.ybb.dto.ResponseResult;
import com.ybb.request.DriverGrabRequest;
import com.ybb.serviceorder.service.GrabService;
import com.ybb.serviceorder.service.OrderService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("grabByRedissonService")
public class GrabByRedissonServiceImpl implements GrabService {

    @Autowired
    private OrderService orderService;
    @Autowired
    private RedissonClient redissonClient;

    @Override
    public ResponseResult orderGrab(DriverGrabRequest request) {
        String key = request.getOrderId() + "";
        RLock lock = redissonClient.getLock(key);
        lock.lock();

        ResponseResult responseResult = orderService.orderGrab(request);

        return responseResult;
    }
}
