package com.taotao.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.taotao.entity.Result;
import com.taotao.pojo.goods.Goods;
import com.taotao.pojo.goods.Sku;
import com.taotao.pojo.goods.Spu;
import com.taotao.service.goods.CategoryService;
import com.taotao.service.goods.ErrorMessageService;
import com.taotao.service.goods.SpuService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.*;

@RestController
@RequestMapping("/item")
public class ItemController {
    @Reference
    private SpuService spuService;
    @Reference
    private CategoryService categoryService;
    @Reference
    private ErrorMessageService errorMessageService;
    @Autowired
    private TemplateEngine templateEngine;
    @Value("${pagePath}")
    private String pagePath;
    private int num=0;
    @GetMapping("/createPage")
    public void createPage(String id){
        //查询商品信息
        Goods goods = spuService.findGoodsById(id);
        Spu spu = goods.getSpu();
        List<Sku> skuList = goods.getSkuList();
        if(skuList!=null){
            List<String> categoryList = new ArrayList<String>();
            if(categoryService.findById(spu.getCategory1Id())!=null)
            categoryList.add(categoryService.findById(spu.getCategory1Id()).getName());//一级分类
            if(categoryService.findById(spu.getCategory2Id())!=null)
            categoryList.add(categoryService.findById(spu.getCategory2Id()).getName());//二级分类
            if(categoryService.findById(spu.getCategory3Id())!=null)
            categoryList.add(categoryService.findById(spu.getCategory3Id()).getName());//三级分类

            Map urlMap = new HashMap();
            for(Sku sku:skuList){
                if("1".equals(sku.getStatus())){
                    String specJSON = JSON.toJSONString(JSON.parseObject(sku.getSpec()), SerializerFeature.MapSortField);//固定JSON元素的位置
                    urlMap.put(specJSON,sku.getId()+".html");
                }
            }

            for(Sku sku:skuList) {
                Context context = null;
                Map<String, Object> dataModel = null;
                Map<String, Object> specMap = null;  //{"颜色":["金色","黑色","蓝色"],"版本":["6GB+64GB"]}
                try {
                    //1.上下文
                    context = new Context();
                    dataModel = new HashMap();
                    dataModel.put("spu", spu);
                    dataModel.put("sku", sku);
                    dataModel.put("categoryList", categoryList);
                    dataModel.put("skuImages", sku.getImages() == null ? new ArrayList<String>() : sku.getImages().split(","));
                    dataModel.put("spuImages", spu.getImages() == null ? new ArrayList<String>() : spu.getImages().split(","));
                    Map<String, Object> paraItems = JSON.parseObject(spu.getParaItems());
                    Map<String, Object> specItems = JSON.parseObject(sku.getSpec());//当前sku规格    {'颜色': '蓝色','版本':'6GB+64GB'}
                    dataModel.put("paraItems", paraItems);
                    dataModel.put("specItems", specItems);


                    specMap = JSON.parseObject(spu.getSpecItems());
                    System.out.println("specMap:"+specMap+"spu:"+spu.getId());
                    for (String key : specMap.keySet()) {//循环规格名称
                        System.out.println("specMap.get(key):"+specMap.get(key));
                        List<String> list = (List<String>) specMap.get(key);
                        List<Map> mapList = new ArrayList<Map>();
                        for (String value : list) {//循环规格列表
                            Map map = new HashMap();
                            map.put("option", value);
                            if (specItems != null && specItems.get(key) != null) {
                                if (specItems.get(key).equals(value)) {
                                    map.put("checked", true);
                                } else {
                                    map.put("checked", false);
                                }
                            }
                            Map spec = JSON.parseObject(sku.getSpec());
                            spec.put(key, value);
                            String specJSON = JSON.toJSONString(spec, SerializerFeature.MapSortField);
                            map.put("url", urlMap.get(specJSON));
                            mapList.add(map);
                        }
                        specMap.put(key, mapList);
                    }


                    dataModel.put("specMap", specMap);
                    context.setVariables(dataModel);
                    //2.准备文件
                    File dir = new File(pagePath);
                    if (!dir.exists()) {
                        dir.mkdirs();//逐级创建文件
                    }
                    File dest = new File(dir, sku.getId() + ".html");
                    //3.生成页面
                    try {
                        PrintWriter writer = new PrintWriter(dest, "UTF-8");
                        templateEngine.process("item", context, writer);
                        System.out.println("num="+(++num));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    //errorMessageService.insert(spu.getId(),sku.getId());
                }
            }
        }

    }

    @GetMapping("/createAllPage")
    public Result createAllPage(){
        for(String id:spuService.findAllId()){
            createPage(id);
        }
        return new Result();
    }



}
