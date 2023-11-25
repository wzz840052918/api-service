package com.kaibai.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.kaibai.utils.SignUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kaibai
 * @date 2023-11-06 21:33
 **/
public class ApiClient {

    private String accessKey;

    private String secretKey;

    public ApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String getName(String name) {
        return HttpRequest.get("http://localhost:8888/name?name=" + name)
                .addHeaders(getRequestHeaderMap())
                .execute().body();
    }

    public String getNameByPost(String name) {
        String myname = HttpUtil.post("http://localhost:8888/name", name);
        return myname;
    }

    private Map<String, String> getRequestHeaderMap() {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("accessKey", accessKey);
        headerMap.put("nonce", RandomUtil.randomNumbers(4));
        headerMap.put("timestamp", String.valueOf(System.currentTimeMillis()));
        headerMap.put("sign", SignUtil.generateSign(secretKey));
        return headerMap;
    }
}
