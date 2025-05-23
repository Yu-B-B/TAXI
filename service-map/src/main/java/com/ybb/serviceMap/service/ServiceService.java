package com.ybb.serviceMap.service;

import com.ybb.dto.ResponseResult;
import com.ybb.serviceMap.remote.ServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceService {
    @Autowired
    private ServiceClient serviceClient;

    public ResponseResult add(String name) {
        return serviceClient.createServer(name);
    }

    public ResponseResult delete(String sid) {
        return serviceClient.delete(sid);
    }

    public ResponseResult get() {
        return serviceClient.get();
    }
}
