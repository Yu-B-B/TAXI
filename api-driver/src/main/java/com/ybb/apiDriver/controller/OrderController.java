package com.ybb.apiDriver.controller;

import com.ybb.dto.ResponseResult;
import com.ybb.dto.TokenResult;
import com.ybb.request.OrderRequest;
import com.ybb.apiDriver.service.OrderService;
import com.ybb.apiDriver.service.PayService;
import com.ybb.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 司机客户端做接到乘客等操作
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private PayService payService;

    // 司机抢单
    @PostMapping("/grab")
    public ResponseResult grab(@RequestBody OrderRequest orderRequest, HttpServletRequest header) {
        String authorization = header.getHeader("Authorization");
        TokenResult token = JwtUtils.resolveToken(authorization);
        String identity = token.getIdentity();
        String driverPhone = token.getPhone();
        String receiveOrderCarLongitude = orderRequest.getReceiveOrderCarLongitude();
        String receiveOrderCarLatitude = orderRequest.getReceiveOrderCarLatitude();

        // 执行抢单
        return orderService.grab(driverPhone,orderRequest.getOrderId(),receiveOrderCarLongitude,receiveOrderCarLatitude);
    }

    // 去接乘客
    @PostMapping("/to-pick-up-passenger")
    public ResponseResult toPickUpPassenger(@RequestBody OrderRequest orderRequest){
        return orderService.toPickUpPassenger(orderRequest);
    }

    // 到达乘客上车点
    @PostMapping("/arrived-departure")
    public ResponseResult arrivedDeparture(@RequestBody OrderRequest orderRequest){
        return orderService.arrivedDeparture(orderRequest);

    }

    // 接到乘客
    /**
     * 司机接到乘客
     * @param orderRequest
     * @return
     */
    @PostMapping("/pick-up-passenger")
    public ResponseResult pickUpPassenger(@RequestBody OrderRequest orderRequest){
        return orderService.pickUpPassenger(orderRequest);
    }

    // 到达目的地，行程终止
    @PostMapping("/passenger-getoff")
    public ResponseResult passengerGetoff(@RequestBody OrderRequest orderRequest){
        return orderService.passengerGetoff(orderRequest);
    }

    // 发起收款，行程结束后调用
    @PostMapping("/push-pay-info")
    public ResponseResult pushPayInfo(@RequestParam Long orderId , @RequestParam String price, @RequestParam Long passengerId){
        return payService.pushPayInfo(orderId,price,passengerId);
    }


























}
