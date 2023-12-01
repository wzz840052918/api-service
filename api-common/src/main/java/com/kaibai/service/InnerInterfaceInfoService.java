package com.kaibai.service;

import com.kaibai.entity.InterfaceInfo;
import org.springframework.stereotype.Component;

/**
 * @author kaibai
 * @date 2023/11/29
 */
@Component
public interface InnerInterfaceInfoService {

    /**
     * 从数据库查询接口是否存在
     * @param url 请求路径
     * @param method 请求方法
     * @return 接口信息
     */
    InterfaceInfo getInterfaceInfo(String url, String method);
}
