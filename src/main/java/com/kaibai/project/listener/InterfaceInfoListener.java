package com.kaibai.project.listener;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.kaibai.entity.InterfaceInfo;
import com.kaibai.project.enums.rocketmq.ApiTagsEnum;
import com.kaibai.project.service.InterfaceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


/**
 * @author kaibai
 * @date 2023/12/10
 */
@RocketMQMessageListener(consumerGroup = "${service.data.rocketmq.apiGroup}", topic = "${service.data.rocketmq.apiTopic}")
@Service
@Slf4j
public class InterfaceInfoListener implements RocketMQListener<MessageExt> {

    @Autowired
    InterfaceInfoService interfaceInfoService;

    @Override
    public void onMessage(MessageExt message) {
        try {
            this.dealWithInterfaceInfo(message);
        } catch (Exception e) {
            log.error("处理用户上传接口异常", e);
        }
    }

    private void dealWithInterfaceInfo(MessageExt message) {
        String tags = message.getTags();
        switch (ApiTagsEnum.valueOf(message.getTags())) {
            // 验证上传接口的可用性
            case USER_UPLOAD_INTERFACE:
                InterfaceInfo interfaceInfo = JSONUtil.toBean(new String(message.getBody()), InterfaceInfo.class);
                try {
                    validUserUploadInterfaceInfo(interfaceInfo);
                } catch (Exception e) {
                    log.error("验证用户上传接口失败", e);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 验证用户上传接口
     * @param interfaceInfo 保存在数据库中的接口信息
     */
    private void validUserUploadInterfaceInfo(InterfaceInfo interfaceInfo) {
        // 1.组装http请求，进行调用
        String method = interfaceInfo.getMethod();
        String url = interfaceInfo.getUrl();
        String requestParams = interfaceInfo.getRequestParams();
        String requestHeader = interfaceInfo.getRequestHeader();
        Map<String, String> requestHeaderMap = JSONUtil.toBean(requestHeader, HashMap.class);
        Map<String, String> requestParamMap = JSONUtil.toBean(requestParams, HashMap.class);
        HttpResponse response = null;
        switch (HttpMethod.valueOf(method)) {
            case POST:
                response = HttpRequest.post(url)
                        .addHeaders(requestHeaderMap)
                        .form(interfaceInfo.getBody())
                        .execute();

                break;
            case GET:
                StringBuilder strBuilder = new StringBuilder();
                for (Map.Entry<String, String> entry : requestParamMap.entrySet()) {
                    strBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                }
                if (strBuilder.length() > 0) {
                    strBuilder.deleteCharAt(strBuilder.length() - 1);
                }
                response = HttpRequest.get(url)
                        .timeout(5000)
                        .addHeaders(requestHeaderMap)
                        .body(strBuilder.toString())
                        .execute();
            default:
                // 一般不会进到这里，保存接口时会有类型判断
                log.error("错误的HttpMethod: [{}]", method);
                break;
        }
        int status = response.getStatus();
        if (HttpStatus.OK.value() == status) {
            // 修改接口状态 ， 将auditStatus设置为1
            interfaceInfoService.updateAuditStatus(interfaceInfo.getId());
            log.info("接口校验完毕，已更改状态: 接口id:{},接口url:{}", interfaceInfo.getId(), url);
        } else {
            log.info("接口校验失败，接口id:{}, 接口url:{}", interfaceInfo.getId(), url);
        }
    }
}
