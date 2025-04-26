package com.ybb.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ybb.constant.CommonStateEnum;
import com.ybb.constant.OrderConstants;
import com.ybb.dto.OrderInfo;
import com.ybb.dto.PriceRule;
import com.ybb.dto.ResponseResult;
import com.ybb.feign.DriverUserFeignClient;
import com.ybb.feign.PriceFeignClient;
import com.ybb.mapper.OrderInfoMapper;
import com.ybb.request.OrderRequest;
import com.ybb.util.RedisPrefixUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class OrderService {
    @Autowired
    private OrderInfoMapper orderMapper;

    @Autowired
    private PriceFeignClient priceFeignClient;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private DriverUserFeignClient driverUserFeignClient;

    public ResponseResult createOrder(OrderRequest orderRequest) {
        // v5 - 判断所属城市是否存在计价规则
        PriceRule priceRule = new PriceRule();
        // 若无法从中获取到两个参数，从fareType中截取获得
        String fareType = orderRequest.getFareType();
        int index = fareType.indexOf("$");
        String cityCode = fareType.substring(0, index);
        String vehicleType = fareType.substring(index + 1);
        priceRule.setCityCode(cityCode);
        priceRule.setVehicleType(vehicleType);
        ResponseResult<Boolean> checkFareRule = priceFeignClient.checkFareRule(priceRule);
        Boolean ifExists = checkFareRule.getData();
        if(!ifExists){
            return ResponseResult.fail(CommonStateEnum.CITY_SERVICE_NOT_SERVICE.getCode(),CommonStateEnum.CITY_SERVICE_NOT_SERVICE.getMessage());
        }

        // v6 - 判断当前城市是否有可用司机
        ResponseResult<Boolean> existsUser = driverUserFeignClient.checkExistsUsefulDriver(cityCode);
        Boolean existsData = existsUser.getData();
        if(!existsData){
            return ResponseResult.fail(CommonStateEnum.CITY_DRIVER_EMPTY.getCode(),CommonStateEnum.CITY_DRIVER_EMPTY.getMessage());
        }


        // v4 - 判断是否为黑名单用户（多个用户在一个设备上参加活动）
        String deviceCode = orderRequest.getDeviceCode();
        String redisKey = RedisPrefixUtils.blackDeviceCodePrefix + deviceCode;

        // 2、采用判断的方式
        Boolean keyExists = stringRedisTemplate.hasKey(redisKey);
        if (keyExists) {
            String value = stringRedisTemplate.opsForValue().get(redisKey);
            Integer count = Integer.parseInt(value);
            if (count > 2) {
                return ResponseResult.fail(CommonStateEnum.DEVICE_IS_BLACK.getCode(), CommonStateEnum.DEVICE_IS_BLACK.getMessage());
            } else {
                stringRedisTemplate.opsForValue().increment(redisKey);
            }
        } else {
            stringRedisTemplate.opsForValue().setIfAbsent(redisKey, "1", 1, TimeUnit.HOURS);
        }

        // 1、若这样先设值，再设置过期时间，【无法保证操作的原子性】
//        stringRedisTemplate.opsForValue().setIfAbsent(redisKey,"1");
//        stringRedisTemplate.expire(redisKey,2, TimeUnit.MINUTES);

        // v3 - 创建订单前做重复校验
        // 当前乘客id下存在1-7的订单状态，不允许下单
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderRequest.getOrderId());
        queryWrapper.and(wrapper -> wrapper
                .eq("order_status", OrderConstants.ORDER_START)
                .eq("order_status", OrderConstants.DRIVER_RECEIVE_ORDER)
                .eq("order_status", OrderConstants.DRIVER_TO_PICK_UP_PASSENGER)
                .eq("order_status", OrderConstants.DRIVER_ARRIVED_DEPARTURE)
                .eq("order_status", OrderConstants.PICK_UP_PASSENGER)
                .eq("order_status", OrderConstants.PASSENGER_GETOFF)
                .eq("order_status", OrderConstants.TO_START_PAY)
        );
        Integer count = orderMapper.selectCount(queryWrapper);
        // 已存在进行中的订单，不允许再创建
        if (count > 0) {
            return ResponseResult.fail(CommonStateEnum.ORDER_GOING_ON.getCode(), CommonStateEnum.ORDER_GOING_ON.getMessage());
        }


        // v2 - 创建订单之前，需要做版本号判断
        ResponseResult<Boolean> checkFareVersion = priceFeignClient.checkFareVersion(orderRequest.getFareType(), orderRequest.getFareVersion());
//        if (checkFareVersion.getCode() == CommonStateEnum.PRICE_RULE_EMPTY.getCode()) {
//            // 未查询出来
//            return ResponseResult.fail(CommonStateEnum.PRICE_RULE_EMPTY.getCode(), CommonStateEnum.PRICE_RULE_EMPTY.getMessage());
//        }
        if (!checkFareVersion.getData()) {
            // 不是最新的
            return ResponseResult.fail(CommonStateEnum.PRICE_RULE_CHANGED.getCode(), CommonStateEnum.PRICE_RULE_CHANGED.getMessage());
        }

        // v1 - 创建订单
        OrderInfo info = new OrderInfo();
        // 熟悉拷贝
        BeanUtils.copyProperties(orderRequest, info);

        info.setOrderStatus(OrderConstants.ORDER_START); // 到这里说明已经开始创建订单了，订单状态可设为开始

        LocalDateTime now = LocalDateTime.now();
        info.setOrderTime(now);
        info.setGmtCreate(now);

        orderMapper.insert(info);
        return ResponseResult.success("");
    }
}
