package com.kaibai.project.enums;

/**
 * @author kaibai
 * @date 2023/11/25
 */
public enum InterfaceAuditStatusEnum {

    NO_AUDIT("未审核", 0),
    AUDITED("审核通过", 1),
    AUDIT_FAILED("审核失败", 2)

    ;

    private final String text;

    private final int status;

    InterfaceAuditStatusEnum(String text, int status) {
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
