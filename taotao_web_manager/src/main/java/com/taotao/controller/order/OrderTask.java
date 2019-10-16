package com.taotao.controller.order;

import com.alibaba.dubbo.config.annotation.Reference;
import com.taotao.service.order.CategoryReportService;
import com.taotao.service.order.OrderService;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class OrderTask {
    /*@Reference
    private OrderService orderService;
    @Scheduled(cron="0 0/2 * * * ?")
    public void orderTimeOutLog(){
        orderService.orderTimeOutLogic();
    }*/

    //每天凌晨一点统计分类销售数量，销售金额数据
    /*@Reference
    private CategoryReportService categoryReportService;
    @Scheduled(cron = "0 0 1 * * ?")
    public void createCategoryReportData(){
        System.out.println("执行任务！");
        categoryReportService.createData();
    }*/
}
