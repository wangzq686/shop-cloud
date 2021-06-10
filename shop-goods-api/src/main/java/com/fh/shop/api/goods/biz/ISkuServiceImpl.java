package com.fh.shop.api.goods.biz;

import com.fh.shop.api.goods.mapper.ISkuMapper;
import com.fh.shop.api.goods.po.Sku;
import com.fh.shop.api.goods.vo.SkuVo;
import com.fh.shop.common.ServerResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wzq
 * @description
 * @date 2021/4/12 12:36
 */
@Service("skuService")
@Transactional(rollbackFor = Exception.class)   //配置事务的回滚
public class ISkuServiceImpl implements ISkuService{
    @Resource
    private ISkuMapper skuMapper;

    @Transactional(readOnly = true) //配置只读事务
    @Override
    public ServerResponse findRecommendNewProductList() {
        List<Sku> skuList= skuMapper.findRecommendNewProductList();
        //给前台返回必要的字段   这样可以提升传输的速度
        List<SkuVo> skuVoList = skuList.stream().map(x -> {
            SkuVo skuVo = new SkuVo();
            skuVo.setId(x.getId());
            skuVo.setSkuName(x.getSkuName());
            skuVo.setPrice(x.getPrice().toString());
            skuVo.setImage(x.getImage());
            return skuVo;
        }).collect(Collectors.toList());
        return ServerResponse.success(skuVoList);
    }

    @Override
    public ServerResponse selectById(Long id) {
        Sku sku = skuMapper.selectById(id);
        return ServerResponse.success(sku);
    }
}
