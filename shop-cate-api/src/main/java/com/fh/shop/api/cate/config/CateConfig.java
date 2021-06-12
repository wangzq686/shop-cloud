package com.fh.shop.api.cate.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * @author wzq
 * @description
 * @date 2021/6/12 20:11
 */
@ConfigurationProperties(prefix = "user")
@Data
public class CateConfig  {
    private String name;
    private Integer age;
    private Book book;
}
