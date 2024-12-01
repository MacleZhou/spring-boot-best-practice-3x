package com.atshuo.audit.service.impl;


import com.atshuo.audit.aop.dto.AuditLogTag;
import com.atshuo.audit.mapper.GoodsMapper;
import com.atshuo.audit.pojo.Goods;
import com.atshuo.audit.service.GoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("goodsService")
public class GoodsServiceImpl extends
        ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    @AuditLogTag(model = "商品模块", tag = "修改商品")
    @Override
    @Transactional
    public Boolean updateGoods(Goods goods) {
        return getBaseMapper().updateById(goods) > 0;
    }
}
