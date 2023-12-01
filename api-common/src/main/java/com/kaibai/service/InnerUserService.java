package com.kaibai.service;


import com.kaibai.entity.User;
import org.springframework.stereotype.Component;

/**
 * @author kaibai
 * @date 2023/11/29
 */
@Component
public interface InnerUserService {
    /**
     * 根据accessKey查询用户
     *
     * @param accessKey AccessKey
     * @return User
     */
    User getInvokeUser(String accessKey);
}
