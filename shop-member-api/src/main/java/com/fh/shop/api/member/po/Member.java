package com.fh.shop.api.member.po;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wzq
 * @description
 * @date 2021/4/20 12:52
 */
@Data
public class Member implements Serializable {
    /*
            | 字段名      | 类型    | 描述   |
            | ---------- | ------- | ------|
            | id         | bigInt  | 主键   |
            | memberName | varcher | 会员名 |
            | password   | varcher | 密码   |
            | nickName   | varcher | 昵称   |
            | phone      | varcher | 电话   |
            | mail       | varcher | 邮箱   |
            | activation | int     | 是否激活   |
            | score | bigInt     | 会员积分   |
            用于短信验证码obj
            确认密码confirmPassword
    */

    /**
     * 主键
     */
    private Long id;
    /**
     * 会员名
     */
    private String memberName;
    /**
     * 密码
     */
    private String password;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 电话
     */
    private String phone;
    /**
     * 邮箱
     */
    private String mail;
    /**
     * 是否激活  0否  1是
     */
    private Integer activation;
    /**
     * 会员积分
     */
    private Long score;
}
