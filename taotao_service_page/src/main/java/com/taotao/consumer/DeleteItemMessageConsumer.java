package com.taotao.consumer;

import com.alibaba.fastjson.JSON;
import com.taotao.pojo.goods.Sku;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.List;
import java.util.Map;

public class DeleteItemMessageConsumer implements MessageListener{
    @Value("${pagePath}")
    private String pagePath;
    public void onMessage(Message message) {
        String SkuIdsString = new String(message.getBody());
        List<String> skuIds = JSON.parseObject(SkuIdsString,List.class);

        for(String skuId:skuIds){
            //删除商品详情文件
            File dir = new File(pagePath);
            File dest = new File(dir,skuId+".html");
            //System.out.println("skuId    "+skuId);
            Boolean f = dest.delete();
            System.out.println("==================="+f);
        }
    }
}
