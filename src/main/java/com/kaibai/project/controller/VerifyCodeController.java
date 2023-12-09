package com.kaibai.project.controller;


import com.kaibai.project.cache.redis.RedisService;
import com.kaibai.project.common.BaseResponse;
import com.kaibai.project.common.ResultUtils;
import com.kaibai.project.utils.EmailUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.kaibai.project.cache.redis.config.RedisPrefixConstant.REGISTER_PREFIX;


/**
 * 发送验证码
 *
 * @author Renvenz
 * @date 2023-06-04 10:36
 **/
@RestController("/sendCode")
@Slf4j
public class VerifyCodeController {

    @Autowired
    EmailUtils emailUtils;

    @Autowired
    RedisService redisService;

    @GetMapping("/email")
    public BaseResponse<Boolean> sendEmailCode(String email) {
        emailUtils.sendVerifyCode(email, REGISTER_PREFIX);
        return ResultUtils.success(true);
    }
}
