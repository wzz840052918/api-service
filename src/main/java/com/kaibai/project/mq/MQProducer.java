package com.kaibai.project.mq;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author kaibai
 * @date 2023/12/10
 */
@Component
public class MQProducer {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public <T> void convertAndSend(T o) {
        // 将对象序列化为json格式并传递

        rocketMQTemplate.convertAndSend("develop-upload-interface" ,o);
    }
}
