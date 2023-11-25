package com.kaibai.project.model.dto.interfaceinfo;

import lombok.Data;

/**
 * 接口调用封装
 *
 * @author kaibai
 * @date 2023/11/25
 */
@Data
public class InterfaceInvokeRequest {

    /**
     *
     */
    private Long id;

    /**
     * 用户请求参数
     */
    private String userRequestParams;

}
