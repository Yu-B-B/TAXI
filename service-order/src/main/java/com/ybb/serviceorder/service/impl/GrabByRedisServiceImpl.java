package com.ybb.serviceorder.service.impl;

import com.ybb.constant.CommonStateEnum;
import com.ybb.dto.ResponseResult;
import com.ybb.request.DriverGrabRequest;
import com.ybb.serviceorder.service.GrabService;
import com.ybb.serviceorder.service.OrderService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service("grabByRedisService")
public class GrabByRedisServiceImpl implements GrabService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RenewRedisLock renewRedisLock;

    @Override
    public ResponseResult orderGrab(DriverGrabRequest request) {
        String orderId = request.getOrderId() + "";
        String driverId = request.getDriverId() + UUID.randomUUID().toString();
        // 加锁与时间控制分开，无法实现原子性
//        Boolean flag = redisTemplate.opsForValue().setIfAbsent(orderId, driverId);
//        redisTemplate.expire(orderId, 30, TimeUnit.MINUTES);

        // v2 锁的设置与时间设置放在一起。-- 锁时间无法控制，出现锁释放混乱（A执行完删除B成功加锁的锁） --
        Boolean flag = redisTemplate.opsForValue().setIfAbsent(orderId, driverId, 20, TimeUnit.SECONDS);
        // 锁的续期
        renewRedisLock.renewLock(orderId,driverId,20);
        ResponseResult grab = null;
        if (flag) {
            try {
                TimeUnit.SECONDS.sleep(30);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            grab = orderService.orderGrab(request);
            System.out.println("释放锁");
            redisTemplate.delete(orderId);
        } else {
            return ResponseResult.fail(CommonStateEnum.ORDER_GRABING.getCode(), CommonStateEnum.ORDER_GRABING.getMessage());
        }
        return grab;
    }
}
