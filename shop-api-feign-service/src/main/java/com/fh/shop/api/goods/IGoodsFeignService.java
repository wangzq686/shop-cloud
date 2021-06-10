package com.fh.shop.api.goods;

import com.fh.shop.api.goods.po.Sku;
import com.fh.shop.common.ServerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author wzq
 * @description
 * @date 2021/6/9 14:31
 */
@FeignClient(name = "shop-goods-api")
public interface IGoodsFeignService {

    @GetMapping("/api/skus/selectById")
    public ServerResponse<Sku> selectById(@RequestParam("id") Long id );

}
