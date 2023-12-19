package com.kaibai.project.model.dto.interfaceinfo;


import lombok.Data;

/**
 * @author kaibai
 * @date 2023-11-04 16:45
 **/
@Data
public class InterfaceInfoAddRequest {
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
    private String url;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;

    /**
     * 请求参数
     */
    private String requestParams;

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
    private String method;

    /**
     * 用户名
     */
    private Long userId;
}
