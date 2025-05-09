package com.ybb.apipassenger.controller;

import com.ybb.dto.ResponseResult;
import com.ybb.response.TerminalListResponse;
import com.ybb.response.TerminalResponse;
import com.ybb.apipassenger.service.TerminalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/terminal")
public class TerminalController {

    @Autowired
    private TerminalService terminalService;

    /**
     * 添加终端
     *
     * @param name
     * @param desc
     * @return
     */
    @PostMapping("/add")
    public ResponseResult<TerminalResponse> add(@RequestParam String name, @RequestParam String desc) {
        return terminalService.add(name, desc);
    }

    @PostMapping("/list")
    public ResponseResult<List<TerminalListResponse>> list(){
        return terminalService.list();
    }

    @PostMapping("/delete")
    public ResponseResult delete(@RequestParam String tid) {
        return terminalService.delete(tid);
    }

    /**
     * 终端搜索
     *
     * @param center
     * @param radius
     * @return
     */
    @PostMapping("/aroundsearch")
    public ResponseResult<List<TerminalResponse>> aroundsearch(String center, Integer radius) {
        return terminalService.aroundsearch(center, radius);
    }


}