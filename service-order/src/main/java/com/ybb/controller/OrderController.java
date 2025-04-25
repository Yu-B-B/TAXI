package com.ybb.controller;

import com.ybb.dto.ResponseResult;
import com.ybb.request.OrderRequest;
import com.ybb.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/createOrder")
    public ResponseResult createOrder(OrderRequest orderRequest) {
        return orderService.createOrder(orderRequest);
    }
}
