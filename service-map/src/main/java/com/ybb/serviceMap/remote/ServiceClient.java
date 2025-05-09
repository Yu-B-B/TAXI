package com.ybb.serviceMap.remote;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ybb.constant.MapConstant;
import com.ybb.dto.DicContent;
import com.ybb.dto.ResponseResult;
import com.ybb.serviceMap.mapper.DicContentMapper;
import com.ybb.response.ServiceResponse;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class ServiceClient {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DicContentMapper dicContentMapper;

    /*
    * POST https://tsapi.amap.com/v1/track/service/add
Content-Type: application/x-www-form-urlencoded

key=f0c42d8524d2eea5e41a445b53f9f38e&name=测试&sid=1047624
    * */
    public ResponseResult createServer(String name) {
        StringBuilder url = new StringBuilder();
        url.append(MapConstant.SERVICE_ADD_URL);
        url.append("?key=" + getAppKey());
        url.append("&name=" + name);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url.toString(), null, String.class);
        String body = responseEntity.getBody();
        JSONObject result = JSONObject.fromObject(body);
        JSONObject data = result.getJSONObject("data");
        String sid = data.getString("sid");
        ServiceResponse serviceResponse = new ServiceResponse();
        serviceResponse.setSid(sid);

        DicContent dicContent = new DicContent();
        dicContent.setType("appSid");
        dicContent.setValue(sid);
        // TODO:需要增加唯一性校验
        dicContentMapper.insert(dicContent);

        return ResponseResult.success(serviceResponse);
    }

    public ResponseResult get() {
        StringBuilder url = new StringBuilder();
        url.append(MapConstant.SERVICE_GET_URL);
        url.append("?key=" + getAppKey());
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url.toString(), null, String.class);
        String body = responseEntity.getBody();
        JSONObject result = JSONObject.fromObject(body);
        JSONObject jsonObject = result.getJSONObject("data");
        JSONArray jsonArray = jsonObject.getJSONArray("results");

        return ResponseResult.success(jsonArray);

    }

    public ResponseResult delete(String sid) {
        StringBuilder url = new StringBuilder();
        url.append(MapConstant.SERVICE_DELETE_URL);
        url.append("?key=" + getAppKey());
        url.append("&sid=" + sid);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url.toString(), null, String.class);
        String body = responseEntity.getBody();
        JSONObject result = JSONObject.fromObject(body);

        return ResponseResult.success(result);
    }

    public String getAppKey(){
        DicContent content = dicContentMapper.selectOne(new LambdaQueryWrapper<DicContent>()
                .eq(DicContent::getType, "appKey"));
        return content.getValue();
    }


}
