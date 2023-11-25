package com.kaibai;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author kaibai
 * @date 2023/11/24
 */
@Configuration
@ConfigurationProperties("kaibai.client")
@Data
@ComponentScan
public class ClientConfig {
    private String accessKey;

    private String secretKey;

}
