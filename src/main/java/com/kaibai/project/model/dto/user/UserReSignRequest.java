package com.kaibai.project.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author kaibai
 * @date 2023-12-08 08:06
 **/
@Data
public class UserReSignRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    private String userAccount;

    /**
     * 邮箱
     */
    private String email;


    private String userPassword;

    private String checkPassword;

    private String validCode;
}
