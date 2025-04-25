package com.ybb.remote;

import com.ybb.constant.MapConstant;
import com.ybb.dto.ResponseResult;
import com.ybb.response.ServiceResponse;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.xml.ws.Response;

@Service
@Slf4j
public class ServiceClient {
    @Value("${amap.app.key}")
    private String appKey;

    @Autowired
    private RestTemplate restTemplate;

    /*
    * POST https://tsapi.amap.com/v1/track/service/add
Content-Type: application/x-www-form-urlencoded

key=f0c42d8524d2eea5e41a445b53f9f38e&name=测试&sid=1047624
    * */
    public ResponseResult createServer(String name) {
        StringBuilder url = new StringBuilder();
        url.append(MapConstant.SERVICE_ADD_URL);
        url.append("?key=" + appKey);
        url.append("&name=" + name);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url.toString(), null, String.class);
        String body = responseEntity.getBody();
        JSONObject result = JSONObject.fromObject(body);
        JSONObject data = result.getJSONObject("data");
        String sid = data.getString("sid");
        ServiceResponse serviceResponse = new ServiceResponse();
        serviceResponse.setSid(sid);

        return ResponseResult.success(serviceResponse);
    }
}
