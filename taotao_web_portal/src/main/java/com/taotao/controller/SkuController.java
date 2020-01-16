package com.taotao.controller;

import com.alibaba.dubbo.config.annotation.Reference;

import com.taotao.entity.Result;
import com.taotao.service.goods.ErrorMessageService;
import com.taotao.service.goods.SkuService;

import com.taotao.service.goods.SpuService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin
@RequestMapping("/sku")
public class SkuController {
    @Reference
    private SkuService skuService;
    @Reference
    private SpuService spuService;
    @Reference
    private ErrorMessageService errorMessageService;
    @GetMapping("/findPrice")
    public Integer findPrice(String id){
        return skuService.findPrice(id);
    }

    @GetMapping("/initSkuDataAll")
    public Result initSkuDataAll(){
        skuService.initSkuData();
        return new Result();
    }

    @GetMapping("/initSkuData")
    public Result initSkuData(String spuId){
        skuService.initSkuData(spuId);
        return new Result();
    }

    @GetMapping("/deleteSpuSpecItems")
    public Result deleteSpuSpecItems(){
        spuService.deleteSpecItems();
        return new Result();
    }

    @GetMapping("/setSkuStatus")
    public Result setSkuStatus(){
        errorMessageService.setSpuStatus();
        return new Result();
    }

}
