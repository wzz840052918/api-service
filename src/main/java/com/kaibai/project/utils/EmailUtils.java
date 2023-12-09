package com.kaibai.project.utils;

import com.kaibai.project.cache.redis.RedisService;
import com.kaibai.project.common.ErrorCode;
import com.kaibai.project.exception.BusinessException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;


/**
 * @author Renvenz
 * @date 2022/8/22
 **/
@Component
public class EmailUtils {

    final RedisService redisService;

    private String host;

    private String username;

    private String password;

    @PostConstruct
    public void init() {
        host = SpringUtils.getRequiredProperty("spring.mail.host");
        username = SpringUtils.getRequiredProperty("spring.mail.username");
        password = SpringUtils.getRequiredProperty("spring.mail.password");
        defaultExpireTime = Long.parseLong(SpringUtils.getRequiredProperty("verify.code.email"));
    }

    /**
     * 默认过期时间
     */
    private long defaultExpireTime = 60*5;

    public EmailUtils(RedisService redisService) {
        this.redisService = redisService;
    }


    public void sendVerifyCode(String email, String prefix) {
        long expire = redisService.getExpire(email);
        if(!isEnoughWaitTime(expire)) {
            throw new BusinessException(ErrorCode.WAIT_FOR_A_MINUTE);
        }
        // 随机生成一个6位数字
        String code = smsCodeGenerator();
        String message = "验证码为:" + code;
        JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
        senderImpl.setHost(host);
        senderImpl.setUsername(username);
        senderImpl.setPassword(password);
        //建立邮件消息,发送简单邮件和html邮件的区别
        MimeMessage mailMessage = senderImpl.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage,"utf-8");
        //设置收件人，寄件人
        try {
            messageHelper.setTo(email);
            messageHelper.setFrom(username);
            messageHelper.setSubject("注册blog验证码");
            messageHelper.setText(message, false);
        } catch (MessagingException e) {
            throw new BusinessException(ErrorCode.MESSAGE_SERVICE_ERROR);
        }
        Properties prop = new Properties();
        //将这个参数设为true，让服务器进行认证,认证用户名和 密码是否正确
        prop.put("mail.smtp.auth","true");
        //设置超时时间
        prop.put("mail.smtp.timeout", "25000");
        senderImpl.setJavaMailProperties(prop);
        //发送邮件
        senderImpl.send(mailMessage);
        redisService.set(prefix + email, code, defaultExpireTime);
    }

    /**
     * 短信验证码 生成规则
     * */
    private String smsCodeGenerator() {
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }

    /**
     * 未等待1min时无法继续发送验证码
     * @param leftOverExpireTime 剩余过期时间
     * @return true/false
     */
    private boolean isEnoughWaitTime(long leftOverExpireTime) {
        return defaultExpireTime - leftOverExpireTime > 60;
    }

    /**
     * 获取验证码
     */
    public String getVerifyCode(String email, String prefix) {
        return (String) redisService.get(prefix + email);
    }
}
