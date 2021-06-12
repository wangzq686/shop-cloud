package com.fh.shop.api.cate.controller;

import com.fh.shop.api.cate.biz.ICateService;
import com.fh.shop.api.cate.config.CateConfig;
import com.fh.shop.api.cate.po.Cate;
import com.fh.shop.common.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "商品分类")
@Controller
@RequestMapping("/api/cates")
@Slf4j
public class CateController {
    @Resource(name = "cateService")
    private ICateService cateService;

    @Value("${server.port}")
    private String port;

//    @Value("${user.name}")
//    private String userName;
//
//    @Value("${user.age}")
//    private Integer userAge;

    @Autowired
    private CateConfig cateConfig;

    @ApiOperation(value = "查询所有分类信息")
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse query(){
//        log.info("端口号信息：{}:{}:{}", port,userName,userAge);
        log.info("端口号信息：{}:{}:{}", port,cateConfig.getName(),cateConfig.getAge());
        System.out.println(cateConfig.getBook().getBookName());
        System.out.println(cateConfig.getBook().getAddr().getAddrName());
        Cate cate = new Cate();

        return cateService.query();
    }



}
