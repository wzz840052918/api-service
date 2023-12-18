package com.kaibai.project.enums.rocketmq;

/**
 * @author kaibai
 * @date 2023-12-18 22:11
 **/
public enum ApiTagsEnum {
    /**
     * 用户上传接口
     */
    USER_UPLOAD_INTERFACE("用户上传接口")

    ;

    private final String description;

    ApiTagsEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
