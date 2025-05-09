package com.ybb.serviceMap.remote;

import com.ybb.constant.MapConstant;
import com.ybb.dto.DicContent;
import com.ybb.dto.ResponseResult;
import com.ybb.serviceMap.mapper.DicContentMapper;
import com.ybb.response.TrackResponse;
import com.ybb.response.TrsearchResponse;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TrackClient {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DicContentMapper dicContentMapper;

    public ResponseResult<TrackResponse> add(String tid){
        Map<String, String> content = getDicContent();

        // &key=<用户的key>
        // 拼装请求的url
        StringBuilder url = new StringBuilder();
        url.append(MapConstant.TRACK_ADD);
        url.append("?key="+ content.get("appKey"));
        url.append("&sid="+ content.get("appSid"));
        url.append("&tid="+tid);
        log.info("高德地图创建轨迹请求："+url);
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(url.toString(), null, String.class);
        String body = stringResponseEntity.getBody();
        log.info("高德地图创建轨迹响应："+body);
        JSONObject result = JSONObject.fromObject(body);
        JSONObject data = result.getJSONObject("data");
        // 轨迹id
        String trid = data.getString("trid");
        // 轨迹名称
        String trname = "";
        if (data.has("trname")){
            trname = data.getString("trname");
        }

        TrackResponse trackResponse = new TrackResponse();
        trackResponse.setTrid(trid);
        trackResponse.setTrname(trname);


        return ResponseResult.success(trackResponse);
    }

    /**
     * 轨迹查询
     *
     * @param tid
     * @param starttime
     * @param endtime
     * @return
     */
    public ResponseResult<TrsearchResponse> trsearch(String tid, Long starttime, Long endtime) {
        Map<String, String> content = getDicContent();

        // 拼装请求的url
        StringBuilder url = new StringBuilder();
        url.append(MapConstant.TERMINAL_TRSEARCH);
        url.append("?key=" + content.get("appKey"));
        url.append("&sid=" + content.get("appSid"));
        url.append("&tid=" + tid);
        url.append("&trid=40");
//        url.append("&starttime=" + starttime);
//        url.append("&endtime=" + endtime);

        System.out.println("高德地图查询轨迹结果请求：" + url.toString());
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url.toString(), String.class);
        System.out.println("高德地图查询轨迹结果响应：" + forEntity.getBody());

        // 解析返回结果
        JSONObject result = JSONObject.fromObject(forEntity.getBody());
        JSONObject data = result.getJSONObject("data");
        int counts = data.getInt("counts");
        if (counts == 0) {
            return null;
        }
        JSONArray tracks = data.getJSONArray("tracks");
        long driveMile = 0L;
        long driveTime = 0L;
        for (int i = 0; i < tracks.size(); i++) {
            JSONObject jsonObject = tracks.getJSONObject(i);

            long distance = jsonObject.getLong("distance");
            driveMile = driveMile + distance;

            long time = jsonObject.getLong("time");
            time = time / (1000 * 60);
            driveTime = driveTime + time;
        }
        TrsearchResponse trsearchResponse = new TrsearchResponse();
        trsearchResponse.setDriveMile(driveMile);
        trsearchResponse.setDriveTime(driveTime);
        return ResponseResult.success(trsearchResponse);
    }

    public ResponseResult delete(String tid, String trid) {
        Map<String, String> content = getDicContent();

        // 拼装请求的url
        StringBuilder url = new StringBuilder();
        url.append(MapConstant.TRACK_DELETE);
        url.append("?key=" + content.get("appKey"));
        url.append("&sid=" + content.get("appSid"));
        url.append("&tid=" + tid);
        url.append("&trid=" + trid);
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url.toString(), String.class);
        System.out.println("高德地图查询轨迹结果响应：" + forEntity.getBody());

        // 解析返回结果
        JSONObject result = JSONObject.fromObject(forEntity.getBody());
        JSONObject data = result.getJSONObject("data");
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
