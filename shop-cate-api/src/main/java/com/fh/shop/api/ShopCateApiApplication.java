package com.fh.shop.api;

import com.fh.shop.api.cate.config.CateConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author wzq
 * @description
 * @date 2021/6/7 12:30
 */

@SpringBootApplication
@MapperScan("com.fh.shop.api.cate.mapper")
@EnableConfigurationProperties({CateConfig.class})
public class ShopCateApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopCateApiApplication.class, args);
    }
}
