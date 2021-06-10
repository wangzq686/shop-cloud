package com.fh.shop.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author wzq
 * @description
 * @date 2021/6/9 14:12
 */
@EnableFeignClients
@SpringBootApplication
public class ShopCartApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShopCartApiApplication.class,args);
    }
}
