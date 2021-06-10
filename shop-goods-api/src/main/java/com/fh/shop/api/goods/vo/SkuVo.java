package com.fh.shop.api.goods.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wzq
 * @description
 * @date 2021/4/12 15:05
 */
@Data
public class SkuVo implements Serializable {

    private Long id;

    private String skuName;

    private String price;

    private String image;

}
