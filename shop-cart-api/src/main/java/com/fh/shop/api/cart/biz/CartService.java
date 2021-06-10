package com.fh.shop.api.cart.biz;

import com.fh.shop.common.ServerResponse;

/**
 * @author wzq
 * @description
 * @date 2021/5/6 14:05
 */
public interface CartService {

    ServerResponse addItem(Long memberId, Long count, Long skuId);

    ServerResponse findCart(Long memberId);

    ServerResponse findCartCount(Long memberId);

    ServerResponse deleteCartItem(Long memberId,Long skuId);

    ServerResponse deleteBatchCartItem(Long memberId, String ids);
}
