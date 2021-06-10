package com.fh.shop.api.member.controller;

import com.alibaba.fastjson.JSON;
import com.fh.shop.api.member.biz.IMemberService;
import com.fh.shop.api.member.vo.MemberVo;
import com.fh.shop.common.Constants;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.util.KeyUtil;
import com.fh.shop.util.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;

/**
 * @author wzq
 * @description
 * @date 2021/6/7 21:34
 */
@RestController
@RequestMapping("/api/members")
@Api(tags = "会员接口")
public class MemberController {

    @Resource
    private IMemberService memberService;

    @Resource
    private HttpServletRequest request;

    /**
     * 取request中当前对象
     * @return
     */
    @SneakyThrows
    @ApiOperation("根据头信息获取会员信息")
    @GetMapping("/findMember")
    @ApiImplicitParam(name = "x-auth", value = "头信息", dataType = "java.lang.String", required = true, paramType = "header")
    public ServerResponse findMember(){
//        MemberVo memberVo = (MemberVo) request.getAttribute(Constants.CURR_MEMBER);
        String JSONStrMemBerVo = URLDecoder.decode(request.getHeader(Constants.CURR_MEMBER),"utf-8");
        MemberVo memberVo = JSON.parseObject(JSONStrMemBerVo, MemberVo.class);
        return ServerResponse.success(memberVo);
    }


    /**
     * 注销
     * @return
     */
    @SneakyThrows
    @GetMapping("/logout")
    public ServerResponse logout(){
//        MemberVo memberVo = (MemberVo) request.getAttribute(Constants.CURR_MEMBER);
        String JSONStrMemBerVo = URLDecoder.decode(request.getHeader(Constants.CURR_MEMBER),"utf-8");
        MemberVo memberVo = JSON.parseObject(JSONStrMemBerVo, MemberVo.class);
        RedisUtil.delete(KeyUtil.buildMemberKey(memberVo.getId()));
        return ServerResponse.success();
    }

    /**
     * 登录
     * @param memberName
     * @param password
     * @return  返回的base64后的签名+秘钥
     */
    @ApiOperation("登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberName", value = "会员名", dataType = "java.lang.String", required = true),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "java.lang.String", required = true)
    })
    @PostMapping("/login")
    public ServerResponse login(String memberName,String password){
        return memberService.login(memberName,password);
    }







}
