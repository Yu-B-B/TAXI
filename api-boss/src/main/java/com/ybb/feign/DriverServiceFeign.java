package com.ybb.feign;

import com.ybb.dto.DriverCarBindingRelationship;
import com.ybb.dto.DriverUser;
import com.ybb.dto.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("server-driver-user")
public interface DriverServiceFeign {
    /**
     * 做【司机】数据插入
     *
     * @param driverUser
     * @return
     */
    @PostMapping("/user/insert")
    ResponseResult insertDriverUser(@RequestBody DriverUser driverUser);

    /**
     * 对【司机】信息做修改
     *
     * @param driverUser
     * @return
     */
    @PostMapping("/user/update")
    ResponseResult updateDriverUser(@RequestBody DriverUser driverUser);

    /**
     * 对【司机】与【车辆】做关系绑定
     *
     * @param driverCarBindingRelationship
     * @return
     */
    @PostMapping(value = "/driver-car-binding-relationship/bind")
    ResponseResult bind(@RequestBody DriverCarBindingRelationship driverCarBindingRelationship);

    /**
     * 对【司机】与【车辆】做关系解绑
     *
     * @param driverCarBindingRelationship
     * @return
     */
    @PostMapping(value = "/driver-car-binding-relationship/unbind")
    ResponseResult unbind(DriverCarBindingRelationship driverCarBindingRelationship);
}
