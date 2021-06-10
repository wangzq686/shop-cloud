package com.fh.shop.api.goods.po;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class Sku implements Serializable {
    private Long id;

    private Long spuId;

    private String skuName;

    private BigDecimal price;

    private Integer stock;

    private String specInfo;

    private Long colorId;

    private String image;

    //是否上架  0 否   1是
    private Integer ifGrounding;

    //是否新品0 否   1是
    private Integer ifNewProduct;

    //是否推荐0 否   1是
    private Integer ifRecommend;

    //销量
    private Long sales;

}
