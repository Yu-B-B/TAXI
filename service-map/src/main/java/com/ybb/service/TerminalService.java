package com.ybb.service;

import com.ybb.dto.ResponseResult;
import com.ybb.remote.TerminalClient;
import com.ybb.response.TerminalResponse;
import com.ybb.response.TrsearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TerminalService {

    @Autowired
    TerminalClient terminalClient;

    public ResponseResult<TerminalResponse> add(String name , String desc){

        return terminalClient.add(name , desc);
    }


    public ResponseResult<List<TerminalResponse>> aroundsearch(String center, Integer radius){

        return terminalClient.aroundsearch(center,radius);
    }

    public ResponseResult<TrsearchResponse> trsearch(String tid , Long starttime , Long endtime){

        return terminalClient.trsearch(tid,starttime,endtime);
    }

}
