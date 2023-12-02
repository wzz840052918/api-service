package com.kaibai.service;

import org.springframework.stereotype.Component;

/**
 * @author kaibai
 * @date 2023/11/29
 */
@Component
public interface InnerInterfaceInfoUserService {

    void invokeCount(Long interfaceId, Long userId);

    int remaining(Long interfaceId, Long userId);
}
