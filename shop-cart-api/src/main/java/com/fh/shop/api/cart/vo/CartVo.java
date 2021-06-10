package com.fh.shop.api.cart.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wzq
 * @description
 * @date 2021/5/6 12:43
 */
@Data
public class CartVo implements Serializable {

    private List<CartItemVo> cartItemVoList=new ArrayList<>();

    private  Long totalCount;

    private  String totalPrice;
}
