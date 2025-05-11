package com.ybb.alipay.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ybb.alipay.feign.OrderFeignClient;
import com.ybb.alipay.mapper.AliPayContentMapper;
import com.ybb.dto.AliPayContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AliPayContentService {
    @Autowired
    private AliPayContentMapper aliPayContentMapper;
    @Autowired
    private OrderFeignClient orderFeignClient;


    public void pay(Long orderId) {

    }

    public Map<String,String> selectAll(){
        Map<String, String> stringStringHashMap = new HashMap<>();
        List<AliPayContent> contents = aliPayContentMapper.selectList(new QueryWrapper<>());
        contents.forEach(content -> {
            stringStringHashMap.put(content.getKeyName(), content.getValueName());
        });
        return stringStringHashMap;
    }

}
