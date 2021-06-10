package com.fh.shop.api.cate.biz;

import com.alibaba.fastjson.JSON;
import com.fh.shop.api.cate.mapper.ICateMapper;
import com.fh.shop.api.cate.po.Cate;
import com.fh.shop.common.ServerResponse;
import com.fh.shop.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("cateService")
@Transactional(rollbackFor = Exception.class)   /*配置事务的回滚*/
public class ICateServiceImpl implements ICateService {
    @Resource
    private ICateMapper cateMapper;

    /**
     * cate获取数据
     * @return
     */
    @Transactional(readOnly = true) /*配置只读事务*/
    @Override
    public ServerResponse query() {
        //先在缓存中查找
        String cateListRedis= RedisUtil.get("cateList");
        //当缓存不为空时
        if(StringUtils.isNoneEmpty(cateListRedis)){
            //将JSON格式的字符串转化为java对象
            List<Cate> cateList = JSON.parseArray(cateListRedis, Cate.class);
            //返回前台
            return ServerResponse.success(cateList);
        }
        //缓存为空才会查询数据库并且往缓存放一份
        List<Cate> cateList=cateMapper.query();
        //将java对象转换为String[JSON格式的字符串]添加到缓存中
        String toJSONString = JSON.toJSONString(cateList);
          //手动更新缓存
//        RedisUtil.set("cateList",toJSONString);
          //自动更新缓存                      30为秒[缓存30秒过期]
        RedisUtil.setEx("cateList",toJSONString,30);
        //返回前台
        return ServerResponse.success(cateList);
    }

}
