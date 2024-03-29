package com.kaibai.project.service;

import com.kaibai.project.model.entity.UserInterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 84005
* @description 针对表【user_interface_info(用户和调用接口关系表)】的数据库操作Service
* @createDate 2023-11-25 23:09:27
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {
    /**
     * 校验
     * @param interfaceInfo interfaceInfo
     * @param condition 条件
     */
    void validInterfaceInfo(UserInterfaceInfo interfaceInfo, boolean condition);

    /**
     * 用户调用统计
     * @param interfaceId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceId, long userId);

    /**
     * 查询当前用户当前接口的剩余调用次数
     * @param interfaceId 接口id
     * @param userId 用户id
     * @return 剩余次数
     */
    int queryRemaining(Long interfaceId, Long userId);
}
