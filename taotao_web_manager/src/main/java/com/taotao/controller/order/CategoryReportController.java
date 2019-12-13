package com.taotao.controller.order;


import com.alibaba.dubbo.config.annotation.Reference;
import com.taotao.pojo.order.CategoryReport;
import com.taotao.service.order.CategoryReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/categoryReport")
public class CategoryReportController {
    @Reference
    private CategoryReportService categoryReportService;

    @GetMapping("/yesterday")
    public List<CategoryReport> yesterday(String date){
        LocalDate localDate = LocalDate.now().minusDays(1);
        //LocalDate localDate1= LocalDate.of(2019,4,15);
        return categoryReportService.categoryReport(localDate);
    }

    @GetMapping("/category1Count")
    public List<Map> categoroy1Count(String date1,String date2){
        return categoryReportService.category1Count(date1, date2);
    }
}
