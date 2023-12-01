package com.kaibai.project.service.impl;

import com.kaibai.entity.User;
import com.kaibai.project.service.UserService;
import com.kaibai.service.InnerUserService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author kaibai
 * @date 2023/11/29
 */
@DubboService
public class InnerUserServiceImpl implements InnerUserService {

    @Resource
    UserService userService;

    @Override
    public User getInvokeUser(String accessKey) {
        return userService.getUserByAccessKey(accessKey);
    }
}
