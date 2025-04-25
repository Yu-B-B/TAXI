package com.ybb.service;

import com.ybb.dto.ResponseResult;
import com.ybb.remote.ServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceService {
    @Autowired
    private ServiceClient serviceClient;

    public ResponseResult add(String name){
        return serviceClient.createServer(name);
    }
}
