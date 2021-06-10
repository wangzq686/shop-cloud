package com.fh.shop.api.cart.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wzq
 * @description
 * @date 2021/5/6 12:39
 */
@Data
public class CartItemVo implements Serializable {

    /**
     * skuid
     */
    private Long id;

    private String skuName;

    private String price;

    private String image;

    private Long count;

    private String subPrice;


}
