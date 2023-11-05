package com.kaibai.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaibai.project.common.ErrorCode;
import com.kaibai.project.exception.BusinessException;
import com.kaibai.project.mapper.InterfaceInfoMapper;
import com.kaibai.project.model.entity.InterfaceInfo;
import com.kaibai.project.model.enums.PostGenderEnum;
import com.kaibai.project.model.enums.PostReviewStatusEnum;
import com.kaibai.project.service.InterfaceInfoService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
* @author wangzhuangzhuang
* @description 针对表【interface_info(存放接口信息)】的数据库操作Service实现
* @createDate 2023-11-04 16:38:41
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService{

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
            if (interfaceInfo == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            String name = interfaceInfo.getName();
            String description = interfaceInfo.getDescription();
            String requestUrl = interfaceInfo.getRequestUrl();
            String requestHeader = interfaceInfo.getRequestHeader();
            String responseHeader = interfaceInfo.getResponseHeader();
            Integer status = interfaceInfo.getStatus();
            Integer method = interfaceInfo.getMethod();
            Long userid = interfaceInfo.getUserid();

            // 创建时，所有参数必须非空
            if (add) {
                if (StringUtils.isAnyBlank(name, description, requestUrl, requestHeader, responseHeader) || ObjectUtils.anyNull(status, method, userid)) {
                    throw new BusinessException(ErrorCode.PARAMS_ERROR);
                }
            }
            if (StringUtils.isNotBlank(name) && name.length() > 8192) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
            }
    }
}




