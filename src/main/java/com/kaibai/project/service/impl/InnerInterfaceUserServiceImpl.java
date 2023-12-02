package com.kaibai.project.service.impl;

import com.kaibai.project.service.UserInterfaceInfoService;
import com.kaibai.service.InnerInterfaceInfoUserService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author kaibai
 * @date 2023/11/29
 */
@DubboService
public class InnerInterfaceUserServiceImpl implements InnerInterfaceInfoUserService {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Override
    public void invokeCount(Long interfaceId, Long userId) {
        userInterfaceInfoService.invokeCount(interfaceId, userId);
    }

    /**
     * 查询当前用户的当前接口剩余调用次数
     * @param interfaceId 接口id
     * @param userId 用户id
     * @return 剩余次数
     */
    @Override
    public int remaining(Long interfaceId, Long userId) {
        return userInterfaceInfoService.queryRemaining(interfaceId, userId);
    }
}
