package com.ybb.serviceorder.service;

import com.ybb.dto.ResponseResult;
import com.ybb.request.DriverGrabRequest;

public interface GrabService {
    ResponseResult orderGrab(DriverGrabRequest request);
}
