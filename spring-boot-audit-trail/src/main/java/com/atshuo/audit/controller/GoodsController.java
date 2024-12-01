package com.atshuo.audit.controller;


import com.atshuo.audit.aop.dto.Result;
import com.atshuo.audit.pojo.Goods;
import com.atshuo.audit.service.GoodsService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Resource
    private GoodsService goodsService;

    @PutMapping
    public Result<Goods> addGoods(@RequestBody Goods goods){
        goods.setCreateTime(LocalDateTime.now());
        goods.setUpdateTime(LocalDateTime.now());
        goodsService.save(goods);
        return Result.success(goods);
    }
    @PostMapping
    public Result<String> updateGoods(@RequestBody Goods goods){
        goods.setUpdateTime(LocalDateTime.now());
        goodsService.updateById(goods);
        return Result.success("更新商品成功");
    }
    @GetMapping("/{id}")
    public Result<Goods> getById(@PathVariable("id") Long goodsId){
        Goods goods = goodsService.getById(goodsId);
        return Result.success(goods);
    }

    @DeleteMapping("/{id}")
    public Result<String> deleteById(@PathVariable("id") Long goodsId){
        boolean flag = goodsService.removeById(goodsId);
        return Result.success(flag?"删除商品成功":"删除商品失败");
    }

}
