package com.ybb.controller;

import com.ybb.constant.HeaderParamConstants;
import com.ybb.dto.OrderInfo;
import com.ybb.dto.ResponseResult;
import com.ybb.request.OrderRequest;
import com.ybb.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class OrderInfoController {
    @Autowired
    private OrderService orderService;

    /**
     * 创建订单
     *
     * @param orderRequest
     * @return
     */
    @PostMapping("/createOrder")
    public ResponseResult createOrder(@RequestBody OrderRequest orderRequest, HttpServletRequest request) {
        //
//        String deviceCode = request.getHeader(HeaderParamConstants.DEVICE_CODE);
//        orderRequest.setDeviceCode(deviceCode);

        return orderService.createOrder(orderRequest);
    }

    /**
     * 循环获取附近终端
     * @param orderInfo
     * @return
     */
    @PostMapping("/dispatchOrder")
    public ResponseResult<Boolean> dispatchOrder(@RequestBody OrderInfo orderInfo) {
        return ResponseResult.success(orderService.dispatchOrder(orderInfo));
    }
}
