package com.kaibai.project.enums;

/**
 * @author kaibai
 * @date 2023/11/25
 */
public enum InterfaceStatusEnum {

    ONLINE("上线", 1),
    OFFLINE("关闭", 0)

    ;

    private final String text;

    private final int status;

    InterfaceStatusEnum(String text, int status) {
        this.text = text;
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public int getStatus() {
        return status;
    }
}
