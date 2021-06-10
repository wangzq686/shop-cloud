package com.fh.shop.api.member.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wzq
 * @description 返回前台的数据
 * @date 2021/4/21 12:10
 */
@Data
public class MemberVo implements Serializable {

    /**
     * 主键
     */
    private Long id;
    /**
     * 会员名
     */
    private String memberName;
    /**
     * 昵称
     */
    private String nickName;

}
