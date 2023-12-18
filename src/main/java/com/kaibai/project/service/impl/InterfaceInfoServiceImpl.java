package com.kaibai.project.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaibai.entity.InterfaceInfo;
import com.kaibai.project.common.ErrorCode;
import com.kaibai.project.enums.InterfaceAuditStatusEnum;
import com.kaibai.project.enums.rocketmq.ApiTagsEnum;
import com.kaibai.project.event.TransactionCommitSendMQEvent;
import com.kaibai.project.exception.BusinessException;
import com.kaibai.project.mapper.InterfaceInfoMapper;
import com.kaibai.project.properties.RocketmqCustomProperties;
import com.kaibai.project.service.InterfaceInfoService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wangzhuangzhuang
 * @description 针对表【interface_info(存放接口信息)】的数据库操作Service实现
 * @createDate 2023-11-04 16:38:41
 */
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements InterfaceInfoService {

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    RocketmqCustomProperties rocketmqCustomProperties;

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String name = interfaceInfo.getName();
        String description = interfaceInfo.getDescription();
        String requestUrl = interfaceInfo.getUrl();
        String requestHeader = interfaceInfo.getRequestHeader();
        String responseHeader = interfaceInfo.getResponseHeader();
        String method = interfaceInfo.getMethod();
        // Long userid = interfaceInfo.getUserid();

        String regex = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(requestUrl);
        boolean b = matcher.matches();
        if (!b) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "非法URL");
        }
        // 创建时，所有参数必须非空
        if (add) {
            if (StringUtils.isAnyBlank(name, description, requestUrl, requestHeader, responseHeader) || ObjectUtils.anyNull(method)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            // url重复校验
            try {
                this.baseMapper.selectOne(new QueryWrapper<InterfaceInfo>().lambda().eq(InterfaceInfo::getUrl, interfaceInfo.getUrl()));
            } catch (Exception e) {
                throw new BusinessException(ErrorCode.DATA_REPEAT_ERROR, "当前请求接口已存在");
            }
        }
        if (StringUtils.isNotBlank(name) && name.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }
    }

    @Override
    public InterfaceInfo getInterfaceInfoByPathAndMethod(String url, String method) {
        if (StringUtils.isAnyBlank(url, method)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("url", url);
        queryWrapper.eq("method", method);
        return this.baseMapper.selectOne(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean uploadInterfaceInfo(InterfaceInfo interfaceInfo) {
        // 1. 保存接口信息
        boolean save = save(interfaceInfo);
        // 2. 发布事务事件，在事务提交后
        applicationEventPublisher.publishEvent(new TransactionCommitSendMQEvent("用户上传接口", rocketmqCustomProperties.getApiTopic(), ApiTagsEnum.USER_UPLOAD_INTERFACE.name(), JSONUtil.toJsonStr(interfaceInfo)));
        return save;
    }

    @Override
    public boolean updateAuditStatus(Long id) {
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setAuditStatus(InterfaceAuditStatusEnum.AUDITED.getStatus());
        return updateById(interfaceInfo);
    }
}




