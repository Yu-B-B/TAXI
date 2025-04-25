package com.ybb.service;

import com.ybb.dto.ResponseResult;
import com.ybb.request.OrderRequest;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    /**
     * 创建订单
     * @param orderRequest
     * @return
     */
    public ResponseResult createOrder(OrderRequest orderRequest) {
        // 调用订单服务创建订单
        return null;
    }

    /**
     * 取消订单
     * @param orderRequest
     * @return
     */
    public ResponseResult cancelOrder(OrderRequest orderRequest) {
        return null;
    }

    /**
     * 订单详情
     * @param orderRequest
     * @return
     */
    public ResponseResult orderDetail(OrderRequest orderRequest) {
        return null;
    }
}
