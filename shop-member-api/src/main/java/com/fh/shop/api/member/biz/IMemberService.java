package com.fh.shop.api.member.biz;

import com.fh.shop.common.ServerResponse;

/**
 * @author wzq
 * @description
 * @date 2021/6/7 21:35
 */
public interface IMemberService {
    ServerResponse login(String memberName, String password);
}
