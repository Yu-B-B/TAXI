package com.ybb.service;

import com.ybb.dto.OrderInfo;
import com.ybb.dto.ResponseResult;
import com.ybb.mapper.OrderInfoMapper;
import com.ybb.request.OrderRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @Autowired
    private OrderInfoMapper orderMapper;

    public ResponseResult createOrder(OrderRequest orderRequest) {
        OrderInfo info = new OrderInfo();
        // 熟悉拷贝
        BeanUtils.copyProperties(orderRequest, info);

        orderMapper.insert(info);
        return ResponseResult.success("");
    }
}
