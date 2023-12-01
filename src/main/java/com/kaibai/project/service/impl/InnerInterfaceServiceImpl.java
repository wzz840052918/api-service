package com.kaibai.project.service.impl;

import com.kaibai.entity.InterfaceInfo;
import com.kaibai.project.service.InterfaceInfoService;
import com.kaibai.service.InnerInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author kaibai
 * @date 2023/11/29
 */
@DubboService
public class InnerInterfaceServiceImpl implements InnerInterfaceInfoService {

    @Resource
    InterfaceInfoService interfaceInfoService;

    @Override
    public InterfaceInfo getInterfaceInfo(String url, String method) {
        return interfaceInfoService.getInterfaceInfoByPathAndMethod(url, method);
    }
}
