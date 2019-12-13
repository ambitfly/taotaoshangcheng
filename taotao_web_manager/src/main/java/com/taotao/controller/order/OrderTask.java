package com.taotao.controller.order;

import org.springframework.stereotype.Component;

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
