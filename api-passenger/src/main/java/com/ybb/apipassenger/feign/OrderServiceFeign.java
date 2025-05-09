package com.ybb.apipassenger.feign;

import com.ybb.dto.ResponseResult;
import com.ybb.request.OrderRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("service-order")
public interface OrderServiceFeign {
    @PostMapping("/createOrder")
    public ResponseResult createOrder(OrderRequest orderRequest);
}
