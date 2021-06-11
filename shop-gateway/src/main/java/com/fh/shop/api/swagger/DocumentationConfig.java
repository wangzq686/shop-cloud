package com.fh.shop.api.swagger;

/**
 * @author wzq
 * @description
 * @date 2021/6/9 21:12
 */

import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * 资源文档配置类
 */
@Component
@Primary
public class DocumentationConfig implements SwaggerResourcesProvider {

    private final RouteLocator routeLocator;

    public DocumentationConfig (RouteLocator routeLocator) {
        this.routeLocator=routeLocator;
    }

    /**
     * 动态获取微服务Swagger接口
     * @return
     */
    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        List<Route> routes= routeLocator.getRoutes();
        routes.forEach(route -> {
            //从各个服务里获取文档
            resources.add(swaggerResource(route.getId(), route.getFullPath().replace("**", "v2/api-docs"), "1.0"));
        });

        return resources;

    }

//手动获取微服务Swagger接口
//    @Override
//    public List<SwaggerResource> get() {
//        List resources = new ArrayList<>();
////        resources.add(swaggerResource("搜索服务接口", "goods/v2/api-docs", "1.0"));// /api/search/是网关路由，/v2/api-docs是swagger中的
//        resources.add(swaggerResource("分类微服务接口", "/cates/v2/api-docs", "1.0"));     //v2是固定的
//        resources.add(swaggerResource("商品微服务接口", "/goods/v2/api-docs", "1.0"));
////        resources.add(swaggerResource("会员微服务接口", "/members/v2/api-docs", "1.0"));
////        resources.add(swaggerResource("订单微服务接口", "/carts/v2/api-docs", "1.0"));
//        return resources;
//    }

    private SwaggerResource swaggerResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }

}
