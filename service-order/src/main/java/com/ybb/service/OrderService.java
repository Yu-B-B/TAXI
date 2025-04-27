package com.ybb.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ybb.config.RedisConfig;
import com.ybb.constant.IdentifyConstant;
import com.ybb.dto.*;
import com.ybb.feign.MapFeignClient;
import com.ybb.constant.CommonStateEnum;
import com.ybb.constant.OrderConstants;
import com.ybb.feign.DriverUserFeignClient;
import com.ybb.feign.PriceFeignClient;
import com.ybb.feign.PushFeignClient;
import com.ybb.mapper.OrderInfoMapper;
import com.ybb.request.OrderRequest;
import com.ybb.request.PriceRuleIsNewRequest;
import com.ybb.request.PushRequest;
import com.ybb.response.OrderDriverResponse;
import com.ybb.response.TerminalResponse;
import com.ybb.util.RedisPrefixUtils;
import javafx.fxml.LoadException;
import net.sf.json.JSONObject;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @Autowired
    private MapFeignClient mapFeignClient;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private PushFeignClient pushFeignClient;

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
        if (!ifExists) {
            return ResponseResult.fail(CommonStateEnum.CITY_SERVICE_NOT_SERVICE.getCode(), CommonStateEnum.CITY_SERVICE_NOT_SERVICE.getMessage());
        }

        // v6 - 判断当前城市是否有可用司机
        ResponseResult<Boolean> existsUser = driverUserFeignClient.checkExistsUsefulDriver(cityCode);
        Boolean existsData = existsUser.getData();
        if (!existsData) {
            return ResponseResult.fail(CommonStateEnum.CITY_DRIVER_EMPTY.getCode(), CommonStateEnum.CITY_DRIVER_EMPTY.getMessage());
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
                .or().eq("order_status", OrderConstants.ORDER_START)
                .or().eq("order_status", OrderConstants.DRIVER_RECEIVE_ORDER)
                .or().eq("order_status", OrderConstants.DRIVER_TO_PICK_UP_PASSENGER)
                .or().eq("order_status", OrderConstants.DRIVER_ARRIVED_DEPARTURE)
                .or().eq("order_status", OrderConstants.PICK_UP_PASSENGER)
                .or().eq("order_status", OrderConstants.PASSENGER_GETOFF)
                .or().eq("order_status", OrderConstants.TO_START_PAY)
        );
        Integer count = orderMapper.selectCount(queryWrapper);
        // 已存在进行中的订单，不允许再创建
        if (count > 0) {
            return ResponseResult.fail(CommonStateEnum.ORDER_GOING_ON.getCode(), CommonStateEnum.ORDER_GOING_ON.getMessage());
        }


        // v2 - 创建订单之前，需要做版本号判断
        PriceRuleIsNewRequest priceRuleIsNewRequest = new PriceRuleIsNewRequest();
        priceRuleIsNewRequest.setFareVersion(orderRequest.getFareVersion());
        priceRuleIsNewRequest.setFareType(fareType);
        ResponseResult<Boolean> checkFareVersion = priceFeignClient.checkFareVersion(priceRuleIsNewRequest);
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

    /**
     * 派发订单
     *
     * @param orderInfo
     */
    public ResponseResult<Boolean> dispatchOrder(OrderInfo orderInfo) {
        // 循环判断范围内是否有对应的车辆
        // 拼接经纬度
        String depLongitude = orderInfo.getDepLongitude(); // 经度
        String depLatitude = orderInfo.getDepLatitude(); // 出发点纬度
        String center = depLatitude + "," + depLongitude;
        ResponseResult<List<TerminalResponse>> aroundsearched = mapFeignClient.aroundsearch(center, 1000);

        List<Integer> list = new ArrayList<>();
        list.add(1000);
        list.add(3000);
        list.add(5000);
        // 增加锚点
        radius:
        for (int i = 0; i < list.size(); i++) {
            Integer radius = list.get(i);
            aroundsearched = mapFeignClient.aroundsearch(center, radius);

            // 解析数据
            List<TerminalResponse> data = aroundsearched.getData();
            for (int j = 0; j < data.size(); j++) {
                TerminalResponse terminalResponse = data.get(j);
                Long carId = terminalResponse.getCarId();

                String longitude = terminalResponse.getLongitude();
                String latitude = terminalResponse.getLatitude();

                // 根据【车辆id】获取对应司机信息，获取可以进行派单的司机信息
                ResponseResult<OrderDriverResponse> result = driverUserFeignClient.getAvailableDriver(carId);
                if (result.getCode() == CommonStateEnum.AVAILABLE_DRIVER_EMPTY.getCode()) {
                    continue;
                } else {
                    // 解析司机ID
                    OrderDriverResponse driverInfo = result.getData();
                    Long driverId = driverInfo.getDriverId();

                    String lockKey = (driverId + "").intern();
                    RLock rLock = redissonClient.getLock(lockKey);
                    rLock.lock();

                    // 获取到司机信息后，若司机处于（接单、接乘客、到达乘客目的地、乘客上车）状态下，不允许接单
                    LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(OrderInfo::getDriverId, driverId);
                    wrapper.and(w ->
                            w.eq(OrderInfo::getOrderStatus, OrderConstants.DRIVER_RECEIVE_ORDER)
                                    .or().eq(OrderInfo::getOrderStatus, OrderConstants.DRIVER_TO_PICK_UP_PASSENGER)
                                    .or().eq(OrderInfo::getOrderStatus, OrderConstants.DRIVER_ARRIVED_DEPARTURE)
                                    .or().eq(OrderInfo::getOrderStatus, OrderConstants.PICK_UP_PASSENGER)
                    );
                    Integer availableDriver = orderMapper.selectCount(wrapper);
                    // 司机处于不可接单的状态下，跳过当前循环
                    if (availableDriver > 0) {
                        rLock.unlock();
                        continue;
                    }
                    // 司机可接单，订单与司机关联
                    orderInfo.setCarId(carId);
                    orderInfo.setDriverId(driverId);
                    orderInfo.setDriverPhone(driverInfo.getDriverPhone());
                    // 获取车辆信息中，在getAvailableDriver可实现一次调用将需要数据全部拿到
                    orderInfo.setReceiveOrderCarLongitude(longitude);
                    orderInfo.setReceiveOrderCarLatitude(latitude);
                    orderInfo.setReceiveOrderTime(LocalDateTime.now());
                    orderInfo.setLicenseId(driverInfo.getLicenseId());
                    orderInfo.setVehicleNo(driverInfo.getVehicleNo());
                    orderInfo.setOrderStatus(OrderConstants.DRIVER_RECEIVE_ORDER);

                    orderMapper.updateById(orderInfo); // 更新订单信息，赋值司机内容

                    // 调用服务推送消息给司机，包含乘客信息，订单信息，起始，终点
                    JSONObject driverContent = new JSONObject();
                    driverContent.put("orderId", orderInfo.getId());
                    driverContent.put("passengerId", orderInfo.getPassengerId());
                    driverContent.put("passengerPhone", orderInfo.getPassengerPhone());
                    driverContent.put("departure", orderInfo.getDeparture());
                    driverContent.put("depLongitude", orderInfo.getDepLongitude());
                    driverContent.put("depLatitude", orderInfo.getDepLatitude());

                    driverContent.put("destination", orderInfo.getDestination());
                    driverContent.put("destLongitude", orderInfo.getDestLongitude());
                    driverContent.put("destLatitude", orderInfo.getDestLatitude());

                    PushRequest pushRequest = new PushRequest();
                    pushRequest.setContent(driverContent.toString());
                    pushRequest.setUserId(driverId);
                    pushRequest.setIdentity(IdentifyConstant.DRIVER);
                    pushFeignClient.push(pushRequest);

                    // 调用服务通知乘客
                    JSONObject passengerContent = new  JSONObject();
                    passengerContent.put("orderId",orderInfo.getId());
                    passengerContent.put("driverId",orderInfo.getDriverId());
                    passengerContent.put("driverPhone",orderInfo.getDriverPhone());
                    passengerContent.put("vehicleNo",orderInfo.getVehicleNo());
                    // 车辆信息，调用车辆服务
                    ResponseResult<Car> carById = driverUserFeignClient.getCarInfo(carId);
                    Car carRemote = carById.getData();

                    passengerContent.put("brand", carRemote.getBrand());
                    passengerContent.put("model",carRemote.getModel());
                    passengerContent.put("vehicleColor",carRemote.getVehicleColor());

                    passengerContent.put("receiveOrderCarLongitude",orderInfo.getReceiveOrderCarLongitude());
                    passengerContent.put("receiveOrderCarLatitude",orderInfo.getReceiveOrderCarLatitude());

                    PushRequest pushRequest1 = new PushRequest();
                    pushRequest1.setUserId(orderInfo.getPassengerId());
                    pushRequest1.setIdentity(IdentifyConstant.PASSENGER);
                    pushRequest1.setContent(passengerContent.toString());

                    pushFeignClient.push(pushRequest1);

                    rLock.unlock();

                    break radius;

                }

            }

        }
        return ResponseResult.success(true);
    }
}
