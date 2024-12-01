package com.atshuo.audit.service;

import com.atshuo.audit.pojo.Goods;
import com.baomidou.mybatisplus.extension.service.IService;

public interface GoodsService extends IService<Goods> {
    Boolean updateGoods(Goods goods);
}
