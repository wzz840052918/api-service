package com.kaibai.project.service;



import com.baomidou.mybatisplus.extension.service.IService;
import com.kaibai.entity.User;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户服务
 *
 * @author kaibai
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @param email 邮箱
     * @param code 验证码
     * @return 新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword, String email, String code);

    /**
     * 用户登录
     *
     * @param email  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    User userLogin(String email, String userPassword, HttpServletRequest request);

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 通过accesskey获取用户信息
     * @param accessKey
     */
    User getUserByAccessKey(String accessKey);

    /**
     * 重新生成AK/SK
     * @param email
     * @param userPassword
     * @param checkPassword
     * @param validCode
     * @return
     */
    long reSign(String email, String userPassword, String checkPassword, String validCode);
}
