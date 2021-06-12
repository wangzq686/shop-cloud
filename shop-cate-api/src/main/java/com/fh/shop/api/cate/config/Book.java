package com.fh.shop.api.cate.config;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wzq
 * @description
 * @date 2021/6/12 20:26
 */
@Data
public class Book implements Serializable {
    private String bookName;

    private Addr addr;
}
