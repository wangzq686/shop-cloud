package com.fh.shop.api.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.shop.api.goods.po.Sku;

import java.util.List;

/**
 * @author wzq
 * @description
 * @date 2021/4/12 12:35
 */
public interface ISkuMapper extends BaseMapper<Sku> {
    List<Sku> findRecommendNewProductList();

}
