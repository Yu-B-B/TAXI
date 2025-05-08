package com.ybb.service;

import com.ybb.dto.ResponseResult;
import com.ybb.remote.TerminalClient;
import com.ybb.response.TerminalListResponse;
import com.ybb.response.TerminalResponse;
import com.ybb.response.TrsearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TerminalService {

    @Autowired
    TerminalClient terminalClient;

    /**
     * 添加终端
     * @param name
     * @param desc
     * @return
     */
    public ResponseResult<TerminalResponse> add(String name , String desc){
        return terminalClient.add(name , desc);
    }


    /**
     * 终端搜索 - 在司机api端调用
     * @param center
     * @param radius
     * @return
     */
    public ResponseResult<List<TerminalResponse>> aroundsearch(String center, Integer radius){
        return terminalClient.aroundsearch(center,radius);
    }

    /**
     * 轨迹查询
     * @param tid
     * @param starttime
     * @param endtime
     * @return
     */
    public ResponseResult<TrsearchResponse> trsearch(String tid , Long starttime , Long endtime){

        return terminalClient.trsearch(tid,starttime,endtime);
    }

    public ResponseResult<List<TerminalListResponse>> list() {
        return terminalClient.list();
    }
}
