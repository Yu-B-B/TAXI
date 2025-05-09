package com.ybb.apipassenger.feign;

import com.ybb.request.PushRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("service-see-push")
public interface PushFeignClient {
    @PostMapping("/push")
    String push(@RequestBody PushRequest pushRequest);
}
