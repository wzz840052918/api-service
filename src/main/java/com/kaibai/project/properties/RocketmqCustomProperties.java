package com.kaibai.project.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * @author kaibai
 * @date 2023-12-18 22:06
 **/
@Component
@Data
@ConfigurationProperties(prefix = "service.data.rocketmq")
public class RocketmqCustomProperties {

    private String apiTopic;

    private String apiGroup;
}
