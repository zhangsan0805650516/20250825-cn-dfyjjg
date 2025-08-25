package com.ruoyi.biz.member.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 修改登录密码参数
 */
@Data
public class LoginPasswordParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 原密码
     */
    private String oldPassword;

    /**
     * 新密码
     */
    private String newPassword1;

    /**
     * 确认新密码
     */
    private String newPassword2;

}
