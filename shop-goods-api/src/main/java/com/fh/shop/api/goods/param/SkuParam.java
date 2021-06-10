package com.fh.shop.api.goods.param;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author wzq
 * @description
 * @date 2021/4/29 21:52
 */
@Data
public class SkuParam implements Serializable {

    private String skuName;

    private BigDecimal price;

    private Integer stock;

    private String brandName;

    private String cateName;
}
