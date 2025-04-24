package com.ybb.controller;

import com.ybb.dto.ResponseResult;
import com.ybb.service.DicDirectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DicDirectionController {
    @Autowired
    private DicDirectionService dicDirectionService;

    @GetMapping("/get-resolve-direction-dic")
    public ResponseResult getResolveDirectionDic() {
        return dicDirectionService.getResolveDirectionDic();
    }
}
