package com.fh.shop.api.cart.biz;

import com.alibaba.fastjson.JSON;
import com.fh.shop.api.cart.vo.CartItemVo;
import com.fh.shop.api.cart.vo.CartVo;
import com.fh.shop.api.goods.IGoodsFeignService;
import com.fh.shop.api.goods.po.Sku;
import com.fh.shop.common.Constants;
import com.fh.shop.common.ResponseEnum;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.util.BigdecimalUtil;
import com.fh.shop.util.KeyUtil;
import com.fh.shop.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author wzq
 * @description
 * @date 2021/5/6 14:05
 */
@Service("cartService")
@Transactional(rollbackFor = Exception.class)   //配置事务的回滚
@Slf4j
public class CartServiceImpl  implements CartService {

    @Autowired
    private IGoodsFeignService goodsFeignService;

    @Value("${sku.ount.limit}")
    private int countLimit;


    /**
     * 添加购物车 cart
     * @param memberId
     * @param count
     * @param skuId
     * @return
     */
    @Override
    public ServerResponse addItem(Long memberId, Long count, Long skuId) {
        //商品购买上限 最大不超过10
        if(count>countLimit){
            return ServerResponse.error(ResponseEnum.CART_SKU_COUNT_LIMIT);
        }
        //非空判断
        if(memberId==null || count==null || skuId==null){
            return ServerResponse.error(ResponseEnum.CART_INFO_IS_NULL);
        }
        //商品是否存在
//        Sku sku = skuMapper.selectById(skuId);



//        ServerResponse serverResponse = goodsFeignService.selectById(skuId);
//        Sku sku = (Sku) serverResponse.getData();
//        log.info("==============={}",sku);
//        System.out.println(sku);


        ServerResponse<Sku> skuServerResponse = goodsFeignService.selectById(skuId);
        Sku sku = skuServerResponse.getData();


        if(sku==null){
            return ServerResponse.error(ResponseEnum.GOODS_IS_NULL);
        }
        //商品是否上架ifGrounding
        if(sku.getIfGrounding().equals(Constants.ifGrounding_if_no)){
            return ServerResponse.error(ResponseEnum.GOODS_GROUNDING_IF_NO);
        }
        //购买的商品数量是否大于库存剩余量
        if(count>sku.getStock()){
            return ServerResponse.error(ResponseEnum.CART_SKU_STOCK_IS_ERROR);
        }

        String key = KeyUtil.buildCartKey(memberId);
        //查询会员是否有购物车
//        String cartJson = RedisUtil.get(key);
        String cartJson = RedisUtil.hget(key, Constants.CART_JSON_FIELD);


        //如果没有购物车
        if(StringUtils.isEmpty(cartJson)){
            //购买数量大于0时
            if(count<0){
                return ServerResponse.error(ResponseEnum.CART_IS_ERROR);
            }

            //创建一个购物车 把商品添加到购物车
            CartItemVo cartItemVo = new CartItemVo();
            CartVo cartVo = new CartVo();
            cartItemVo.setCount(count);
            cartItemVo.setId(skuId);
            cartItemVo.setImage(sku.getImage());
            cartItemVo.setSkuName(sku.getSkuName());
            String priceStr = sku.getPrice().toString();
            cartItemVo.setPrice(priceStr);


            BigDecimal subPrice = BigdecimalUtil.mul(priceStr, count +"");
            cartItemVo.setSubPrice(subPrice.toString());

            cartVo.getCartItemVoList().add(cartItemVo);
            cartVo.setTotalCount(count);
            cartVo.setTotalPrice(cartItemVo.getSubPrice());

            //更新Redis中的购物车
//            RedisUtil.set(key, JSON.toJSONString(cartVo));
            RedisUtil.hset(key, Constants.CART_JSON_FIELD,JSON.toJSONString(cartVo));
            RedisUtil.hset(key,Constants.CART_COUNT_FIELD,count+"");
        }else {
            //如果有购物车
            CartVo cartVo = JSON.parseObject(cartJson, CartVo.class);
            List<CartItemVo> cartItemVoList = cartVo.getCartItemVoList();

            Optional<CartItemVo> itemVo = cartItemVoList.stream().filter(x -> x.getId().longValue() == skuId.longValue()).findFirst();

            if(itemVo.isPresent()){
                //购物车有这款商品，找到这款商品，更新商品的数量和小计
                CartItemVo cartItemVo = itemVo.get();
                long itemCount = count + cartItemVo.getCount();
                cartItemVo.setCount(itemCount);
                //商品购买上限
                if(itemCount>countLimit){
                    return ServerResponse.error(ResponseEnum.CART_SKU_COUNT_LIMIT);
                }
                if(itemCount<=0){
                    //购买数量小于0时从Redis删除
                    cartItemVoList.removeIf(x-> x.getId().longValue()==cartItemVo.getId().longValue());
                    if(cartItemVoList.size()==0){
                        RedisUtil.delete(key);  //??
                        return ServerResponse.success();
                    }
                    //更新
                    updateCart(key, cartVo);
                }

                BigDecimal subPrice = new BigDecimal(cartItemVo.getSubPrice());
                String subPriceStr  = subPrice.add(BigdecimalUtil.mul(String.valueOf(cartItemVo.getPrice()), count + "")).toString();
                cartItemVo.setSubPrice(subPriceStr);
                updateCart(key, cartVo);
            }else {
                //商品数量不能小于0
                if(count<0){
                    return ServerResponse.error(ResponseEnum.CART_IS_ERROR);
                }
                //购物车中没有这款商品，直接将商品加入购物车
                CartItemVo cartItemVo = new CartItemVo();
                cartItemVo.setCount(count);
                cartItemVo.setPrice(sku.getPrice().toString());
                cartItemVo.setId(sku.getId());
                cartItemVo.setImage(sku.getImage());
                cartItemVo.setSkuName(sku.getSkuName());

                BigDecimal subPrice = BigdecimalUtil.mul(sku.getPrice().toString(), count + "");

                cartItemVo.setSubPrice(subPrice.toString());
                cartVo.getCartItemVoList().add(cartItemVo);
                updateCart(key, cartVo);

            }
        }

        return ServerResponse.success();
    }

    private void updateCart(String key, CartVo cartVo) {
        //更新购物车
        List<CartItemVo> cartItemVos = cartVo.getCartItemVoList();
        long totalCount = 0;
        BigDecimal totalPrice = new BigDecimal(0);
        for (CartItemVo vo : cartItemVos) {
            totalCount += vo.getCount();
            totalPrice = totalPrice.add(new BigDecimal(vo.getSubPrice()));
        }
        cartVo.setTotalCount(totalCount);
        cartVo.setTotalPrice(totalPrice.toString());
        // 更新购物车【redis中得购物车】
//        RedisUtil.set(key, JSON.toJSONString(cartVo));
        RedisUtil.hset(key, Constants.CART_JSON_FIELD, JSON.toJSONString(cartVo));
        RedisUtil.hset(key,Constants.CART_COUNT_FIELD,totalCount+"");
    }

    /**
     * 查询会员购物车商品
     * @param memberId
     * @return
     */
    @Override
    public ServerResponse findCart(Long memberId) {
        String key = KeyUtil.buildCartKey(memberId);
//        String cartJSON = RedisUtil.get(key);
        String cartJSON = RedisUtil.hget(key, Constants.CART_JSON_FIELD);
        if(StringUtils.isEmpty(cartJSON)){
            return ServerResponse.error(ResponseEnum.CART_IS_NULL);
        }
        CartVo cartVo = JSON.parseObject(cartJSON, CartVo.class);
        return ServerResponse.success(cartVo);
    }

    /**
     *查询会员购物车商品个数
     * @param memberId
     * @return
     */
    @Override
    public ServerResponse findCartCount(Long memberId) {
        System.out.println("-------------------------------===============================-----------------------------");
        System.out.println(memberId);
        String key = KeyUtil.buildCartKey(memberId);
        String cartCount = RedisUtil.hget(key, Constants.CART_COUNT_FIELD);
        return ServerResponse.success(cartCount);
    }

    @Override
    public ServerResponse deleteCartItem(Long memberId,Long skuId) {
        //先获取会员对应的购物车
        String key = KeyUtil.buildCartKey(memberId);
        String cartJson = RedisUtil.hget(key, Constants.CART_JSON_FIELD);
        CartVo cartVo = JSON.parseObject(cartJson, CartVo.class);
        List<CartItemVo> cartItemVoList = cartVo.getCartItemVoList();
        cartItemVoList.removeIf(x->x.getId().longValue()==skuId.longValue());
        //如果购物车数量等于0就把购物车删除
        if(cartItemVoList.size()==0){
            RedisUtil.delete(key);
            return ServerResponse.success();
        }
        //更新redis购物车
        updateCart(key, cartVo);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse deleteBatchCartItem(Long memberId, String ids) {
        if(StringUtils.isEmpty(ids)){
            return ServerResponse.error(ResponseEnum.CART_IS_ERROR);
        }
        String key = KeyUtil.buildCartKey(memberId);
        String cartJson = RedisUtil.hget(key, Constants.CART_JSON_FIELD);
        CartVo cartVo = JSON.parseObject(cartJson, CartVo.class);
        List<CartItemVo> cartItemVoList = cartVo.getCartItemVoList();
        Arrays.stream(ids.split(",")).forEach(x->cartItemVoList.removeIf(y->y.getId().longValue()==Long.parseLong(x)));
        if(cartItemVoList.size()==0){
            RedisUtil.delete(key);
            return ServerResponse.success();
        }
        //更新redis购物车
        updateCart(key, cartVo);
        return ServerResponse.success();
    }
}
