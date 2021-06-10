package com.fh.shop.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author wzq
 * @description
 * @date 2021/6/7 21:28
 */
@SpringBootApplication
@MapperScan("com.fh.shop.api.member")
public class ShopMemberApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShopMemberApiApplication.class,args);
    }
}
