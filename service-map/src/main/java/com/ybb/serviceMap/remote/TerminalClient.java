package com.ybb.serviceMap.remote;

import com.ybb.constant.MapConstant;
import com.ybb.dto.DicContent;
import com.ybb.dto.ResponseResult;
import com.ybb.serviceMap.mapper.DicContentMapper;
import com.ybb.response.TerminalListResponse;
import com.ybb.response.TerminalResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TerminalClient {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DicContentMapper dicContentMapper;

    /**
     * 在已创建的服务上添加终端
     *
     * @param name
     * @param desc
     * @return
     */
    public ResponseResult<TerminalResponse> add(String name, String desc) {
        Map<String, String> content = getDicContent();
        // &key=<用户的key>
        // 拼装请求的url
        StringBuilder url = new StringBuilder();
        url.append(MapConstant.TERMINAL_ADD);
        url.append("?key=" + content.get("appKey"));
        url.append("&sid=" + content.get("appSid"));
        url.append("&name=" + name);
        url.append("&desc=" + desc);
        System.out.println("创建终端请求：" + url.toString());
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(url.toString(), null, String.class);
        System.out.println("创建终端响应：" + stringResponseEntity.getBody());
        /**
         * {
         *     "data": {
         *         "name": "车辆2",
         *         "tid": 583145283,
         *         "sid": 797498
         *     },
         *     "errcode": 10000,
         *     "errdetail": null,
         *     "errmsg": "OK"
         * }
         */
        String body = stringResponseEntity.getBody();
        JSONObject result = JSONObject.fromObject(body);
        JSONObject data = result.getJSONObject("data");
        String tid = data.getString("tid");

        TerminalResponse terminalResponse = new TerminalResponse();
        terminalResponse.setTid(tid);

        return ResponseResult.success(terminalResponse);
    }


    /**
     * 对已上传的终端做半径查询
     *
     * @param center
     * @param radius
     * @return
     */
    public ResponseResult<List<TerminalResponse>> aroundsearch(String center, Integer radius) {
        Map<String, String> content = getDicContent();

        StringBuilder url = new StringBuilder();
        url.append(MapConstant.TERMINAL_AROUNDSEARCH);
        url.append("?key=" + content.get("appKey"));
        url.append("&sid=" + content.get("appSid"));
        url.append("&center=" + center);
        url.append("&radius=" + radius);

        System.out.println("终端搜索请求：" + url.toString());
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(url.toString(), null, String.class);
        System.out.println("终端搜索响应：" + stringResponseEntity.getBody());

        // 解析终端搜索结果
        String body = stringResponseEntity.getBody();
        JSONObject result = JSONObject.fromObject(body);
        JSONObject data = result.getJSONObject("data");

        List<TerminalResponse> terminalResponseList = new ArrayList<>();

        JSONArray results = data.getJSONArray("results");
        for (int i = 0; i < results.size(); i++) {
            TerminalResponse terminalResponse = new TerminalResponse();

            JSONObject jsonObject = results.getJSONObject(i);
            // desc是carId，
            String desc = jsonObject.getString("desc");
            Long carId = Long.parseLong(desc);
            String tid = jsonObject.getString("tid");

            JSONObject location = jsonObject.getJSONObject("location");
            String longitude = location.getString("longitude");
            String latitude = location.getString("latitude");

            terminalResponse.setCarId(carId);
            terminalResponse.setTid(tid);
            terminalResponse.setLongitude(longitude);
            terminalResponse.setLatitude(latitude);

            terminalResponseList.add(terminalResponse);
        }


        return ResponseResult.success(terminalResponseList);
    }



    public ResponseResult<List<TerminalListResponse>> list() {
        Map<String, String> content = getDicContent();
        StringBuilder url = new StringBuilder();
        url.append(MapConstant.TERMINAL_LIST);
        url.append("?key=" + content.get("appKey"));
        url.append("&sid=" + content.get("appSid"));

        ResponseEntity<String> forEntity = restTemplate.getForEntity(url.toString(), String.class);
        JSONObject jsonObject = JSONObject.fromObject(forEntity.getBody());
        JSONObject dataResult = jsonObject.getJSONObject("data");
        JSONArray results = dataResult.getJSONArray("results");

        List<TerminalListResponse> result = new ArrayList<>();
        results.forEach(item -> {
            JSONObject obj = (JSONObject) item;
            TerminalListResponse response = new TerminalListResponse();
            response.setName(obj.getString("name"));
            response.setTid(obj.getLong("tid"));
            response.setDesc(obj.getString("desc"));
            response.setCreatetime(obj.getLong("createtime"));
            response.setLocatetime(obj.getLong("locatetime"));
            result.add(response);
        });
        return ResponseResult.success(result);
    }

    public ResponseResult delete(String tid) {
        Map<String, String> content = getDicContent();
        StringBuilder url = new StringBuilder();
        url.append(MapConstant.TERMINAL_DELETE);
        url.append("?key=" + content.get("appKey"));
        url.append("&sid=" + content.get("appSid"));
        url.append("&tid=" + tid);

        ResponseEntity<String> forEntity = restTemplate.getForEntity(url.toString(), String.class);
        return null;
    }

    public Map<String, String> getDicContent() {
        List<DicContent> dicContents = dicContentMapper.selectList(null);
        Map<String, String> result = new HashMap<>();
        dicContents.forEach(item -> {
            result.put(item.getType(), item.getValue());
        });
        return result;
    }



}
