package com.taotao.controller.goods;

import com.alibaba.dubbo.config.annotation.Reference;

import com.taotao.service.goods.StockBackService;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class SkuTask {
    @Reference
    private StockBackService stockBackService;

    @Scheduled(cron = "0 0 0/1 * * ?")
    public void backTask(){
        System.out.println("执行回滚任务！");
        stockBackService.doBack();
    }
}
