package com.fh.shop.api.cart.controller;

import com.fh.shop.api.BaseFeignController;
import com.fh.shop.api.cart.biz.CartService;
import com.fh.shop.api.member.vo.MemberVo;
import com.fh.shop.common.Constants;
import com.fh.shop.common.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author wzq
 * @description
 * @date 2021/5/6 14:04
 */
@Api(tags = "购物车相关接口")
@RestController
@RequestMapping("/api/carts/")
public class CartController extends BaseFeignController {

    @Resource(name = "cartService")
    private CartService cartService;

    @Resource
    private HttpServletRequest request;

    /**
     * 添加购物车
     * @param count
     * @param skuId
     * @return
     */
    @PostMapping("/addItem")
    @ApiOperation("添加商品到购物车")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "skuId", value = "商品id", dataType = "java.lang.Long", required = true,example = "0"),
            @ApiImplicitParam(name = "count", value = "商品数量", dataType = "java.lang.Long", required = true,example = "0"),
            @ApiImplicitParam(name = "x-auth", value = "头信息", dataType = "java.lang.String", required = true, paramType = "header")
    })
    public ServerResponse addItem(Long count,Long skuId){
        MemberVo memberVo = buildMemberVo(request);
        //获取当前登录会员的id
        Long memberId = memberVo.getId();

        return cartService.addItem(memberId,count,skuId);
    }

    /**
     * 查询会员购物车商品
     * @return
     */
    @GetMapping("/findCart")
    @ApiOperation("查询会员购物车商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "x-auth", value = "头部信息", dataType = "java.lang.String", required = true, paramType = "header")
    })
    public ServerResponse findCart() {
        MemberVo memberVo = buildMemberVo(request);
        Long memberId = memberVo.getId();
        return cartService.findCart(memberId);
    }

    /**
     * 查询会员购物车商品个数
     * @return
     */
    @GetMapping("/findCartCount")
    @ApiOperation("查询会员购物车商品个数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "x-auth", value = "头部信息", dataType = "java.lang.String", required = true, paramType = "header")
    })
    public ServerResponse findCartCount() {
        MemberVo memberVo = buildMemberVo(request);
        Long memberId = memberVo.getId();
        return cartService.findCartCount(memberId);
    }

    /**
     * 购物车商品删除
     * @return
     */
    @DeleteMapping("/deleteCartItem")
    @ApiOperation("查询会员购物车商品个数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "x-auth", value = "头部信息", dataType = "java.lang.String", required = true, paramType = "header",example = "0")
    })
    public ServerResponse deleteCartItem(Long skuId) {
        MemberVo memberVo = buildMemberVo(request);
        Long memberId = memberVo.getId();
        return cartService.deleteCartItem(memberId,skuId);
    }

    /**
     * 购物车商品批量删除
     * @return
     */
    @DeleteMapping("/deleteBatchCartItem")
    @ApiOperation("查询会员购物车商品个数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "x-auth", value = "头部信息", dataType = "java.lang.String", required = true, paramType = "header")
    })
    public ServerResponse deleteBatchCartItem(String ids) {
        MemberVo memberVo = buildMemberVo(request);
        Long memberId = memberVo.getId();
        return cartService.deleteBatchCartItem(memberId,ids);
    }



}
