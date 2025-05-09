package com.ybb.service;

import com.ybb.dto.ResponseResult;
import com.ybb.remote.TrackClient;
import com.ybb.response.TrackResponse;
import com.ybb.response.TrsearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrackService {

    @Autowired
    private TrackClient trackClient;

    public ResponseResult<TrackResponse> add(String tid){

        return trackClient.add(tid);
    }

    /**
     * 轨迹查询
     * @param tid
     * @param starttime
     * @param endtime
     * @return
     */
    public ResponseResult<TrsearchResponse> trsearch(String tid , Long starttime , Long endtime){
        return trackClient.trsearch(tid,starttime,endtime);
    }

    /**
     * 轨迹删除
     * @param tid 设备唯一编号
     * @param trid 轨迹id
     * @return
     */
    public ResponseResult delete(String tid, String trid) {
        return trackClient.delete(tid,trid);
    }
}
