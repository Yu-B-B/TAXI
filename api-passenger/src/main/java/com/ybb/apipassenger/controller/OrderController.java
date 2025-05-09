package com.ybb.apipassenger.controller;

import com.ybb.dto.ResponseResult;
import com.ybb.request.OrderRequest;
import com.ybb.apipassenger.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 乘客下单-实时单
     * @param orderRequest
     * @return
     */
    @PostMapping("/create")
    public ResponseResult createOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.createOrder(orderRequest);
    }

    /**
     * 乘客下单-预约单
     * @param orderRequest
     * @return
     */
    @PostMapping("/bookingOrder")
    public ResponseResult bookingOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.bookingOrder(orderRequest);
    }
}
