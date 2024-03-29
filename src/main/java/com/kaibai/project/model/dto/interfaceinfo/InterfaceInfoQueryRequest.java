package com.kaibai.project.model.dto.interfaceinfo;

import com.kaibai.project.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * @author kaibai
 * @date 2023-11-04 16:47
 **/
@Data
public class InterfaceInfoQueryRequest extends PageRequest implements Serializable {
    /**
     * 接口名称
     */
    private String name;

    /**
     * 接口描述
     */
    private String description;

    /**
     * 接口请求地址
     */
    private String requestUrl;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;

    /**
     * 请求体
     */
    private String body;

    /**
     * 接口状态（0-关闭，1-开启）
     */
    private Integer status;

    /**
     * 请求人类型
     */
    private Integer method;

    /**
     * 用户名
     */
    private Long userId;
}
