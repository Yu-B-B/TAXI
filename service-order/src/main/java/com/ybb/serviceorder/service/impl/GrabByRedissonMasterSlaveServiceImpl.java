package com.ybb.serviceorder.service.impl;

import com.ybb.dto.ResponseResult;
import com.ybb.request.DriverGrabRequest;
import com.ybb.serviceorder.service.GrabService;
import com.ybb.serviceorder.service.OrderService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("grabByRedissonMasterSlaveService")
public class GrabByRedissonMasterSlaveServiceImpl implements GrabService {

    @Autowired
    private OrderService orderService;

    @Autowired
    @Qualifier("redissonMSYml")
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
