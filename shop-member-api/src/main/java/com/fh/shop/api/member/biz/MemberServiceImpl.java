package com.fh.shop.api.member.biz;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fh.shop.api.member.mapper.IMemberMapper;
import com.fh.shop.api.member.po.Member;
import com.fh.shop.api.member.vo.MemberVo;
import com.fh.shop.common.Constants;
import com.fh.shop.common.ResponseEnum;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.util.KeyUtil;
import com.fh.shop.util.Md5Util;
import com.fh.shop.util.RedisUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author wzq
 * @description
 * @date 2021/6/7 21:35
 */
@Service("memberService")
public class MemberServiceImpl implements IMemberService{
    @Resource
    private IMemberMapper memberMapper;

    @Override
    public ServerResponse login(String memberName, String password) {
        //非空判断
        if(StringUtils.isEmpty(memberName) || StringUtils.isEmpty(password)){
            return ServerResponse.error(ResponseEnum.MEMBER_LOGIN_IS_NULL);
        }
        //根据名称查询数据
        QueryWrapper<Member> memberQueryWrapper = new QueryWrapper<>();
        memberQueryWrapper.eq("memberName", memberName);
        Member memberSelectOne = memberMapper.selectOne(memberQueryWrapper);
        //当根据名称查询为空时
        if(memberSelectOne==null){
            return ServerResponse.error(ResponseEnum.MEMBER_LOGIN_MEM_NAME_NOT_EXIST);
        }
        //验证密码
        if(!Md5Util.md5(password).equals(memberSelectOne.getPassword())){
            return ServerResponse.error(ResponseEnum.MEMBER_LOGIN_PASSWORD_IS_ERROR);
        }
        //验证是否激活
        if(memberSelectOne.getActivation()==0){
            //发送邮件
            //生成激活码
            String id = String.valueOf(memberSelectOne.getId());
            String mail = memberSelectOne.getMail();
            String uuid = UUID.randomUUID().toString();
            String key = KeyUtil.buildActivationKey(uuid);
            HashMap<String, String> map = new HashMap<>();
            map.put("id",id);
            map.put("mail",mail);
            String url="http://localhost:8087/api/member/activationMember?uuid=";
            RedisUtil.setEx(key,id,5*60);
//            mailUtil.sendMailHtml(memberSelectOne.getMail(),"会员激活","请点击链接进行激活"+url+uuid);
            return ServerResponse.error(ResponseEnum.MEMBER_ACTIVATION_IS_NOT,map);
        }
        //创建MemberVo
        MemberVo memberVo = new MemberVo();
        memberVo.setId(memberSelectOne.getId());
        memberVo.setMemberName(memberSelectOne.getMemberName());
        memberVo.setNickName(memberSelectOne.getNickName());
        //将用户信息转为json
        String JSONStrMemBerVo = JSON.toJSONString(memberVo);
        //生成签名（签名不能包含密码！！！）
        String md5Sign = Md5Util.md5(JSONStrMemBerVo + Constants.SECRET);
        //将用户信息和签名进行base64转码
        String JSONStrMemBerVoBase64 = Base64.getEncoder().encodeToString(JSONStrMemBerVo.getBytes(StandardCharsets.UTF_8));
        String md5SignBase64 = Base64.getEncoder().encodeToString(md5Sign.getBytes(StandardCharsets.UTF_8));
        //将信息存入redis中
        Long id = memberSelectOne.getId();
        RedisUtil.setEx(KeyUtil.buildMemberKey(id),"",Constants.TOKEN_EXPIRE);
        //将base64转码后的 用户和签名通过 . 拼接 返回前台
        return ServerResponse.success(JSONStrMemBerVoBase64+"."+md5SignBase64);
    }
}
