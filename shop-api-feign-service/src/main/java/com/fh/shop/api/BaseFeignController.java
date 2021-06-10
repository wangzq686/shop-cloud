package com.fh.shop.api;

import com.alibaba.fastjson.JSON;
import com.fh.shop.api.member.vo.MemberVo;
import com.fh.shop.common.Constants;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author wzq
 * @description
 * @date 2021/6/9 16:19
 */
public class BaseFeignController {

    public MemberVo buildMemberVo(HttpServletRequest request){
        try {
            //和拦截器不是一个项目，所有不能再request中获取会员信息，在请求头中获取
            // 解决编码问题,先在拦截器编码，再搁这里解码
            String JSONStrMemBerVo = URLDecoder.decode(request.getHeader(Constants.CURR_MEMBER),"utf-8");
            MemberVo memberVo = JSON.parseObject(JSONStrMemBerVo, MemberVo.class);
            return memberVo;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


}
