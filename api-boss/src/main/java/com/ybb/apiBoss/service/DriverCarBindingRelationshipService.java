package com.ybb.apiBoss.service;

import com.ybb.dto.DriverCarBindingRelationship;
import com.ybb.dto.ResponseResult;
import com.ybb.apiBoss.feign.DriverServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DriverCarBindingRelationshipService {

    @Autowired
    private DriverServiceFeign driverServiceFeign;


    /**
     * 【司机】与【车辆】绑定
     * @param driverCarBindingRelationship
     * @return
     */
    public ResponseResult bind(DriverCarBindingRelationship driverCarBindingRelationship){
        return driverServiceFeign.bind(driverCarBindingRelationship);
    }

    public ResponseResult unbind(DriverCarBindingRelationship driverCarBindingRelationship) {

        return driverServiceFeign.unbind(driverCarBindingRelationship);
    }
}