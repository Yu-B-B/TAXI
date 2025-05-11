package com.ybb.apiDriver.service;

import com.ybb.constant.IdentifyConstant;
import com.ybb.dto.ResponseResult;
import com.ybb.apiDriver.feign.PushFeignClient;
import com.ybb.apiDriver.feign.ServerOrderFeignClient;
import com.ybb.request.OrderRequest;
import com.ybb.request.PushRequest;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayService {
    @Autowired
    PushFeignClient pushFeignClient;

    @Autowired
    ServerOrderFeignClient serviceOrderClient;

    public ResponseResult pushPayInfo(Long orderId, String price, Long passengerId){
        // 封装消息
        JSONObject message = new JSONObject();
        message.put("price",price); // 价格
        message.put("orderId",orderId); // 订单
        // 修改订单状态
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderId(orderId);
        serviceOrderClient.pushPayInfo(orderRequest);

        PushRequest pushRequest = new PushRequest();
        pushRequest.setContent(message.toString());
        pushRequest.setUserId(passengerId);
        pushRequest.setIdentity(IdentifyConstant.PASSENGER);

        // 推送消息
        pushFeignClient.push(pushRequest);

        return ResponseResult.success();
    }
}
