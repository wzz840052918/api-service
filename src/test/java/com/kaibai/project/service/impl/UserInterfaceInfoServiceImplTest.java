package com.kaibai.project.service.impl;

import com.kaibai.project.service.UserInterfaceInfoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
@SpringBootTest
public class UserInterfaceInfoServiceImplTest {

    @Resource
    UserInterfaceInfoService userInterfaceInfoService;

    @Test
    public void invokeCount() {
        boolean a = userInterfaceInfoService.invokeCount(1, 1);
        Assertions.assertTrue(a);
    }
}