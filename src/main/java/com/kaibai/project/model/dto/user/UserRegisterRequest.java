package com.kaibai.project.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author kaibai
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    private String userAccount;

    /**
     * 邮箱
     */
    private String email;


    private String userPassword;

    private String checkPassword;

    private String code;
}
