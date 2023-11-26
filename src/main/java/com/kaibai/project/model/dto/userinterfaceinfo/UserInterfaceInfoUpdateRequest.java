package com.kaibai.project.model.dto.userinterfaceinfo;

import lombok.Data;

/**
 * @author kaibai
 * @date 2023/11/25
 */
@Data
public class UserInterfaceInfoUpdateRequest {

    private Long id;

    /**
     * 总调用次数
     */
    private Integer total_num;

    /**
     * 接口剩余调用次数
     */
    private Integer left_num;

    /**
     * 0-正常，1-禁用
     */
    private Integer status;
}
