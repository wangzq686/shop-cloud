package com.fh.shop.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author wzq
 * @description
 * @date 2021/6/7 12:30
 */
@SpringBootApplication
@EnableSwagger2
@MapperScan("com.fh.shop.api.goods.mapper")
public class ShopGoodsApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopGoodsApiApplication.class, args);
    }
}
