package com.ybb.serviceMap.controller;

import com.ybb.dto.ResponseResult;
import com.ybb.response.TrsearchResponse;
import com.ybb.serviceMap.service.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 轨迹接口，在车辆初始化时进行调用
 */
@RestController
@RequestMapping("/track")
public class TrackController {

    @Autowired
    TrackService trackService;

    /**
     * 轨迹创建，创建轨迹后才能上传坐标
     * @param tid
     * @return
     */
    @PostMapping("/add")
    public ResponseResult add(String tid){
        return trackService.add(tid);
    }

    @PostMapping("/delete")
    public ResponseResult delete(String tid,String trid){
        return trackService.delete(tid,trid);
    }

    /**
     * 轨迹查询
     *
     * @param tid
     * @param starttime 开始时间 - 毫秒
     * @param endtime   结束时间 - 毫秒
     * @return
     */
    @PostMapping("/trsearch")
    public ResponseResult<TrsearchResponse> trsearch(String tid, Long starttime, Long endtime) {
        return trackService.trsearch(tid, starttime, endtime);
    }
}
