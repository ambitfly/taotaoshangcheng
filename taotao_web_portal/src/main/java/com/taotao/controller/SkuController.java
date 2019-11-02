package com.taotao.controller;

import com.alibaba.dubbo.config.annotation.Reference;

import com.taotao.service.goods.SkuService;

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

    @GetMapping("/findPrice")
    public Integer findPrice(String id){
        return skuService.findPrice(id);
    }



}
