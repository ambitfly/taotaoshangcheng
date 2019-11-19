package com.taotao.consumer;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import com.taotao.pojo.goods.Sku;
import com.taotao.pojo.goods.Spu;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateItemMessageConsumer implements MessageListener{
    @Value("${pagePath}")
    private String pagePath;

    @Autowired
    private TemplateEngine templateEngine;

    public void onMessage(Message message) {
        String jsonStringSpuMap = new String(message.getBody());
        Map<String,String> spuMap = JSON.parseObject(jsonStringSpuMap,Map.class);
       // System.out.println(spuMap);
        createPage(spuMap);
    }

    public void createPage(Map<String,String> spuMap){
        //查询商品信息

        Spu spu = JSON.parseObject(spuMap.get("spu"),Spu.class);
        List<String> skuList = JSON.parseObject(spuMap.get("skuList"),List.class);
        List<String> categoryList = JSON.parseObject(spuMap.get("categoryList"),List.class);


        Map urlMap = new HashMap();
        for(String skuString:skuList){
            Sku sku = JSON.parseObject(skuString,Sku.class);
            if("1".equals(sku.getStatus())){
                String specJSON = JSON.toJSONString(JSON.parseObject(sku.getSpec()), SerializerFeature.MapSortField);//固定JSON元素的位置
                urlMap.put(specJSON,sku.getId()+".html");
            }
        }

        for(String skuString:skuList){
            Sku sku = JSON.parseObject(skuString,Sku.class);
            //1.上下文
            Context context = new Context();
            Map<String,Object> dataModel = new HashMap();
            dataModel.put("spu",spu);
            dataModel.put("sku",sku);
            dataModel.put("categoryList",categoryList);
            dataModel.put("skuImages",sku.getImages()==null?new ArrayList<String>():sku.getImages().split(","));
            dataModel.put("spuImages",spu.getImages()==null?new ArrayList<String>():spu.getImages().split(","));
            Map<String,Object> paraItems = JSON.parseObject(spu.getParaItems());
            Map<String,Object> specItems = JSON.parseObject(sku.getSpec());//当前sku规格    {'颜色': '蓝色','版本':'6GB+64GB'}
            dataModel.put("paraItems",paraItems);
            dataModel.put("specItems",specItems);


            Map<String,Object> specMap = JSON.parseObject(spu.getSpecItems());  //{"颜色":["金色","黑色","蓝色"],"版本":["6GB+64GB"]}

            for(String key:specMap.keySet()){//循环规格名称
                List<String> list = (List<String>)specMap.get(key);
                List<Map> mapList = new ArrayList<Map>();
                for(String value:list){//循环规格列表
                    Map map = new HashMap();
                    map.put("option",value);
                    if(specItems.get(key).equals(value)){
                        map.put("checked",true);
                    }else{
                        map.put("checked",false);
                    }
                    Map spec = JSON.parseObject(sku.getSpec());
                    spec.put(key,value);
                    String specJSON = JSON.toJSONString(spec,SerializerFeature.MapSortField);
                    map.put("url",urlMap.get(specJSON));
                    mapList.add(map);
                }

                specMap.put(key,mapList);
            }

            dataModel.put("specMap",specMap);
            context.setVariables(dataModel);
            //2.准备文件
            File dir = new File(pagePath);
            if(!dir.exists()){
                dir.mkdirs();//逐级创建文件
            }
            File dest = new File(dir,sku.getId()+".html");
            //3.生成页面
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(dest,"UTF-8");
                templateEngine.process("item",context,writer);
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                writer.close();
            }
        }

    }
}
