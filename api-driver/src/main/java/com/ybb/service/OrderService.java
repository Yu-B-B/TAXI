package com.ybb.service;

import com.ybb.dto.ResponseResult;
import com.ybb.feign.ServerOrderFeignClient;
import com.ybb.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 调用订单服务中各类接口
 */
@Service
public class OrderService {
    @Autowired
    private ServerOrderFeignClient serverOrderFeignClient;

    /**
     * 去接乘客
     * @param orderRequest
     * @return
     */
    public ResponseResult toPickUpPassenger(OrderRequest orderRequest) {
        return serverOrderFeignClient.changeStatus(orderRequest);
    }


    /**
     * 到达乘客上车点
     * @param orderRequest
     * @return
     */
    public ResponseResult arrivedDeparture(OrderRequest orderRequest) {
        return serverOrderFeignClient.arrivedDeparture(orderRequest);
    }


    public ResponseResult pickUpPassenger(OrderRequest orderRequest) {
        return serverOrderFeignClient.pickUpPassenger(orderRequest);
    }

    public ResponseResult passengerGetoff(OrderRequest orderRequest) {
        return serverOrderFeignClient.passengerGetOff(orderRequest);
    }
}
