package com.fh.shop.api.goods.biz;

import com.fh.shop.api.goods.param.SkuParam;
import com.fh.shop.common.ServerResponse;

import java.util.List;


/**
 * @author wzq
 * @description
 * @date 2021/4/12 12:36
 */
public interface ISkuService {
    ServerResponse findRecommendNewProductList();

    ServerResponse selectById(Long id);
}
