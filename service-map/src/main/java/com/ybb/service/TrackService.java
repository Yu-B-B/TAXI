package com.ybb.service;

import com.ybb.dto.ResponseResult;
import com.ybb.remote.TrackClient;
import com.ybb.response.TrackResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrackService {

    @Autowired
    private TrackClient trackClient;

    public ResponseResult<TrackResponse> add(String tid){

        return trackClient.add(tid);
    }
}
