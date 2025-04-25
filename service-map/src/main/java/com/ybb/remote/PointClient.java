package com.ybb.remote;

import com.ybb.constant.MapConstant;
import com.ybb.dto.ResponseResult;
import com.ybb.request.PointDTO;
import com.ybb.request.PointRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class PointClient {

    @Value("${amap.app.key}")
    private String amapKey;

    @Value("${amap.app.sid}")
    private String amapSid;

    @Autowired
    private RestTemplate restTemplate;

    public ResponseResult upload (PointRequest pointRequest){
        // &key=<用户的key>
        // 拼装请求的url
        StringBuilder url = new StringBuilder();
        url.append(MapConstant.POINT_UPLOAD);
        url.append("?key="+amapKey);
        url.append("&sid="+amapSid);
        url.append("&tid="+pointRequest.getTid());
        url.append("&trid="+pointRequest.getTrid());
        url.append("&points=");
        PointDTO[] points = pointRequest.getPoints();
        url.append("%5B"); // 符号转义[
        for (PointDTO p : points
             ) {
            url.append("%7B");// 符号转义{
            String locatetime = p.getLocatetime();
            String location = p.getLocation();
            url.append("%22location%22"); // 符号转义\
            url.append("%3A"); // 符号转义:
            url.append("%22"+location+"%22");
            url.append("%2C"); // 符号转义,

            url.append("%22locatetime%22");
            url.append("%3A");
            url.append(locatetime);

            url.append("%7D"); // 符号转义}
        }
        url.append("%5D"); // 符号转义]

        System.out.println("上传位置请求："+url.toString());
        // 编码处理，防止异常
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(URI.create(url.toString()), null, String.class);
        System.out.println("上传位置响应："+stringResponseEntity.getBody());

        return ResponseResult.success();
    }
}
