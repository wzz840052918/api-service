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

    private final String HOST = "http://localhost:8090";

    public ApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String getName(String name) {
        return HttpRequest.get(HOST + "/api/name?name=" + name)
                .addHeaders(getRequestHeaderMap())
                .execute().body();
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
