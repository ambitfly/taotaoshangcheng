package com.taotao.service.impl;

import com.taotao.service.goods.CategoryService;
import com.taotao.service.goods.SkuService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Init implements InitializingBean{
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SkuService skuService;
    public void afterPropertiesSet() throws Exception {
        System.out.println("缓存预热！");
        categoryService.saveCategoryTreeToRedis();
        skuService.saveAllPriceToRedis();
    }
}
