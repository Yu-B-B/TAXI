package com.ybb.apipassenger.service;

import com.ybb.dto.ResponseResult;
import com.ybb.remote.TerminalClient;
import com.ybb.response.TerminalListResponse;
import com.ybb.response.TerminalResponse;
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



    public ResponseResult<List<TerminalListResponse>> list() {
        return terminalClient.list();
    }

    public ResponseResult delete(String tid) {
        return terminalClient.delete(tid);
    }
}
