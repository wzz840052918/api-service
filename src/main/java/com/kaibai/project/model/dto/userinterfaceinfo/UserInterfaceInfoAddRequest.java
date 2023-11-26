package com.kaibai.project.model.dto.userinterfaceinfo;

import lombok.Data;

/**
 * @author kaibai
 * @date 2023/11/25
 */
@Data
public class UserInterfaceInfoAddRequest {
    /**
     * 要开通接口的用户id
     */
    private Long userId;

    /**
     * 接口id
     */
    private Long interfaceInfoId;

    /**
     * 接口总调用次数
     */
    private Integer totalNum;

    /**
     * 接口剩余调用次数
     */
    private Integer leftNum;
}
