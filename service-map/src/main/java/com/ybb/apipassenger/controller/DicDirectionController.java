package com.ybb.apipassenger.controller;

import com.ybb.dto.ResponseResult;
import com.ybb.apipassenger.service.DicDirectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DicDirectionController {
    @Autowired
    private DicDirectionService dicDirectionService;

    /**
     * 获取高德地图中的地区列表信息
     * @return
     */
    @GetMapping("/get-resolve-direction-dic")
    public ResponseResult getResolveDirectionDic() {
        return dicDirectionService.getResolveDirectionDic();
    }
}
