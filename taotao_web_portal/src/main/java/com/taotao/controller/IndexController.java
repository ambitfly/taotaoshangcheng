package com.taotao.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.taotao.pojo.business.Ad;
import com.taotao.service.business.AdService;
import com.taotao.service.goods.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class IndexController {
    @Reference
    private AdService adService;
    @Reference
    private CategoryService categoryService;


    @GetMapping("/index")
    public String index(Model model){
        //轮播图
        List<Ad> ads =  adService.findByPosition("web_index_lb");
        model.addAttribute("lbt",ads);
        //商品分类
        List<Map> categoryList = categoryService.findCategoryTree();
        model.addAttribute("categoryList",categoryList);
        return "index";
    }
}
