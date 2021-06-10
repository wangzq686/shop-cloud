package com.fh.shop.api.filter;

import com.alibaba.fastjson.JSON;
import com.fh.shop.common.Constants;
import com.fh.shop.common.ResponseEnum;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.util.KeyUtil;
import com.fh.shop.util.Md5Util;
import com.fh.shop.util.RedisUtil;
import com.fh.shop.vo.MemberVo;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.List;

/**
 * @author wzq
 * @description
 * @date 2021/6/8 14:36
 */
@Component
@Slf4j
public class JwtFilter extends ZuulFilter {

    @Value("${fh.shop.checkUrls}")
    private List<String> checkUrls;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @SneakyThrows
    @Override
    public Object run() throws ZuulException {
//        log.info("执行了JwtFilter==={}", checkUrls);
        //获取当前访问的Url
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();

        StringBuffer requestURL = request.getRequestURL();
        boolean isCheck=false;
        for (String checkUrl : checkUrls) {
            if(requestURL.indexOf(checkUrl)>0){
                isCheck=true;
                break;
            }
        }
      if(!isCheck){
            //默认相当于放行
            return null;
        }
      //需要进行验证的
        //验证用户信息和签名[x-auth:eyJpZCI6MSwibWVtYmVyTmFtZSI6Indhbmd6cSIsIm5pY2tOYW1lIjoi546L5oyv5peXIn0=.Mjk1Yjk3ZmM5NzAyNmJiOGZhZDFlOWE3ZGUxZjNhYWQ=]
        //判断是否有头信息
        String header = request.getHeader("x-auth");
        //判断头部信息为空时 将不在继续执行 给前台返回提示
        if(StringUtils.isEmpty(header)){
            return buildResponse(ResponseEnum.TOKEN_IS_MISS);

        }
        //验证头部信息是否完整
        String[] headerArr = header.split("\\.");
        //判断头信息 （分割后的数组长度为2）
        if(headerArr.length!=2){
            return buildResponse(ResponseEnum.TOKEN_IS_NOT_FULL);
        }
        //验签【核心】
        String JSONStrMemBerVoBase64 = headerArr[0];
        String md5SignBase64= headerArr[1];
        //进行base64解码
        String JSONStrMemBerVo = new String(Base64.getDecoder().decode(JSONStrMemBerVoBase64), "utf-8");
        String md5Sign = new String(Base64.getDecoder().decode(md5SignBase64), "utf-8");
        //验证签名是否一致
        //获取新签名
        String newSign = Md5Util.sign(JSONStrMemBerVo, Constants.SECRET);
        //判断新生成的签名是否和传过来的签名是否一致
        if(!newSign.equals(md5Sign)){
            return buildResponse(ResponseEnum.TOKEN_IS_FALL);
        }
        //将json转为java对象
        MemberVo memberVo = JSON.parseObject(JSONStrMemBerVo, MemberVo.class);
        Long id = memberVo.getId();
        //判断是否key的存活时间是否过期
        if(!RedisUtil.exist(KeyUtil.buildMemberKey(id))){
            return buildResponse(ResponseEnum.TOKEN_IS_TIME_OUT);
        }
        //续签（重新设置过期时间）
        RedisUtil.expire(KeyUtil.buildMemberKey(id), Constants.TOKEN_EXPIRE);
        //将memberVo存入request中
//        request.setAttribute(Constants.CURR_MEMBER, memberVo);

        //将要传给后台微服务的信息放到请求头中
        currentContext.addZuulRequestHeader(Constants.CURR_MEMBER, URLEncoder.encode(JSONStrMemBerVo,"utf-8"));

        return null;
    }

    private Object buildResponse(ResponseEnum responseEnum) {
        RequestContext currentContext = RequestContext.getCurrentContext();


        HttpServletResponse response = currentContext.getResponse();
        //解决返回编码问题
        response.setContentType("application/json:charset=utf-8");

        //不仅拦截了【不往后面走了】并且还能给前台提示
        //拦截了就不会再进行路由转发了
        currentContext.setSendZuulResponse(false);
        //提示json信息
        ServerResponse error = ServerResponse.error(responseEnum);
        String res = JSON.toJSONString(error);
        currentContext.setResponseBody(res);
        //返回空
        return null;
    }


}
