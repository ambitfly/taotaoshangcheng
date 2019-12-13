package com.taotao.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.taotao.dao.CategoryReportMapper;
import com.taotao.pojo.order.CategoryReport;
import com.taotao.service.order.CategoryReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CategoryReportService.class)
public class CategoryReportServiceImpl implements CategoryReportService {
    @Autowired
    CategoryReportMapper categoryReportMapper;

    @Transactional
    @Override
    public void createData() {
        LocalDate localDate = LocalDate.now().minusDays(1);
        //LocalDate localDate = LocalDate.of(2019,4,15);
        List<CategoryReport> categoryReports = categoryReportMapper.categoryReport(localDate);
        for (CategoryReport categoryReport : categoryReports) {
            categoryReportMapper.insert(categoryReport);
        }
    }

    @Override
    public List<CategoryReport> categoryReport(LocalDate date) {

        return categoryReportMapper.categoryReport(date);

    }

    @Override
    public List<Map> category1Count(String date1, String date2) {
        return categoryReportMapper.category1Count(date1, date2);
    }
}
