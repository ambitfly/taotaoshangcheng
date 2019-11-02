package com.taotao.service.impl;

import com.taotao.service.business.AdService;
import com.taotao.service.goods.CategoryService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Init implements InitializingBean{
    @Autowired
    private AdService adService;

    public void afterPropertiesSet() throws Exception {
        System.out.println("缓存预热！");
        adService.saveAllAdtoRedis();
    }
}
