package com.kaibai.project.model.dto.userinterfaceinfo;

import com.kaibai.project.common.PageRequest;
import lombok.Data;

/**
 * @author kaibai
 * @date 2023/11/25
 */
@Data
public class UserInterfaceInfoQueryRequest extends PageRequest {

    /**
     * 根据主键查询
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 接口调用总次数
     */
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;

    /**
     * 接口状态
     */
    private Integer status;
}
