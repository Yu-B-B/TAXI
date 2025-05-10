package com.ybb.serviceorder.service.impl;

import com.ybb.constant.CommonStateEnum;
import com.ybb.dto.ResponseResult;
import com.ybb.request.DriverGrabRequest;
import com.ybb.serviceorder.service.GrabService;
import com.ybb.serviceorder.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service("grabByRedisLuaService")
public class GrabByRedisLuaServiceImpl implements GrabService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RenewRedisLock renewRedisLock;

    @Autowired
    private DefaultRedisScript<Boolean> redisSetScript;
    @Autowired
    private DefaultRedisScript<Boolean> redisDelScript;


    @Override
    public ResponseResult orderGrab(DriverGrabRequest request) {
        String orderId = request.getOrderId() + "";
        String driverId = request.getDriverId() + "";
        driverId = driverId + "-" + UUID.randomUUID();
        // 加锁与时间控制分开，无法实现原子性
//        Boolean flag = redisTemplate.opsForValue().setIfAbsent(orderId, driverId);
//        redisTemplate.expire(orderId, 30, TimeUnit.MINUTES);

        // v2 锁的设置与时间设置放在一起。-- 锁时间无法控制，出现锁释放混乱（A执行完删除B成功加锁的锁） --
//        Boolean flag = redisTemplate.opsForValue().setIfAbsent(orderId, driverId, 20, TimeUnit.SECONDS);

        // v3 采用lua脚本做加锁和锁续期
        List<String> strings = Arrays.asList(orderId, driverId);
        Boolean flag = redisTemplate.execute(redisSetScript, strings, "30");

        ResponseResult grab = null;
        if (flag) {
            renewRedisLock.renewLock(orderId, driverId, 20);
            grab = orderService.orderGrab(request);
//            redisTemplate.delete(orderId);
            redisTemplate.execute(redisDelScript, Arrays.asList(orderId), driverId);
        } else {
            return ResponseResult.fail(CommonStateEnum.ORDER_GRABING.getCode(), CommonStateEnum.ORDER_GRABING.getMessage());
        }
        return grab;
    }
}
