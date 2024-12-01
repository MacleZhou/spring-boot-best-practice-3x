package com.atshuo.audit;

import com.atshuo.audit.pojo.Goods;
import com.atshuo.audit.service.GoodsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AuditApplicationTests {

    @Autowired
    GoodsService goodsService;

    @Test
    void contextLoads() {
    }

    @Test
    public void testUpdateGoods() {
        Goods goods = goodsService.getBaseMapper().selectById(1749086365418647554L);
        goods.setBrand("华为");
        goodsService.updateGoods(goods);
    }
}
