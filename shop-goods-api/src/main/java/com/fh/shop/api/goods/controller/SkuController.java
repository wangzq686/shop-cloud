package com.fh.shop.api.goods.controller;

import com.fh.shop.api.goods.biz.ISkuService;
import com.fh.shop.common.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author wzq
 * @description
 * @date 2021/4/12 12:38
 */
@Api(tags = "商品sku")
@RestController
@RequestMapping("/api/skus")
public class SkuController {

    @Resource(name = "skuService")
    private ISkuService skuService;

    /**
     *
     * @return
     */
    @GetMapping("/findRecommendNewProductList")
    @ApiOperation(value = "查询的是推荐新品上架商品")
    private ServerResponse findRecommendNewProductList(){
        return skuService.findRecommendNewProductList();
    }

    @ApiOperation(value = "根据id查询数据")
    @GetMapping("/selectById")
    public ServerResponse selectById(@RequestParam("id") Long id ){
        return skuService.selectById(id);
    }

}
