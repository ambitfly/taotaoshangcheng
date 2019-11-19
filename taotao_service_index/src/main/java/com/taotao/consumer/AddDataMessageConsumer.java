package com.taotao.consumer;

import com.alibaba.fastjson.JSON;
import com.taotao.pojo.goods.Sku;
import com.taotao.util.RestClientFactory;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddDataMessageConsumer implements MessageListener {
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    public void onMessage(Message message) {
        String skuMapString = new String(message.getBody());
        Map<String,String> skuMap = JSON.parseObject(skuMapString,Map.class);
        List<String> skuListString = JSON.parseObject(skuMap.get("skuList"),List.class);
        List<Sku> skuList = new ArrayList<Sku>();
        for(String skuString:skuListString){
            Sku sku = JSON.parseObject(skuString,Sku.class);
            skuList.add(sku);
        }
        importSku(skuList);
    }

    private void importSku(List<Sku> skuList) {
        //2.封装请求对象
            BulkRequest bulkRequest = new BulkRequest();
            for (Sku sku : skuList) {
                IndexRequest indexRequest = new IndexRequest("sku", "doc", sku.getId());
                Map<String, Object> skuMap = new HashMap();

                skuMap.put("name", sku.getName());
                skuMap.put("brandName", sku.getBrandName());
                skuMap.put("categoryName", sku.getCategoryName());
                skuMap.put("price", sku.getPrice());
                skuMap.put("createTime", sku.getCreateTime());
                skuMap.put("saleNum", sku.getSaleNum());
                skuMap.put("image",sku.getImage());
                skuMap.put("commentNum", sku.getCommentNum());
                Map<String, Object> spec = JSON.parseObject(sku.getSpec());
                skuMap.put("spec", spec);

                indexRequest.source(skuMap);
                bulkRequest.add(indexRequest);
            }


        //3.获取响应结果
        int status = 0;
        try {
            BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest);
            status = bulkResponse.status().getStatus();
            System.out.println("addStatus:"+status);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                restHighLevelClient.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
