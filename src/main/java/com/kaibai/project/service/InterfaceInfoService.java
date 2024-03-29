package com.kaibai.project.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.kaibai.entity.InterfaceInfo;

/**
* @author wangzhuangzhuang
* @description 针对表【interface_info(存放接口信息)】的数据库操作Service
* @createDate 2023-11-04 16:38:41
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    /**
     * 校验
     * @param interfaceInfo interfaceInfo
     * @param condition 条件
     */
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean condition);

    InterfaceInfo getInterfaceInfoByPathAndMethod(String path, String method);

    /**
     * 开发者发布接口信息
     * @param interfaceInfo 接口信息
     * @return true/false
     */
    boolean uploadInterfaceInfo(InterfaceInfo interfaceInfo);

    /**
     * 更新接口的审核状态
     * @param id 接口id
     * @return true/false
     */
    boolean updateAuditStatus(Long id);
}
