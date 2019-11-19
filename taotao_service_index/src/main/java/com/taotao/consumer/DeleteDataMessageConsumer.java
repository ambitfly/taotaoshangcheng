package com.taotao.consumer;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

public class DeleteDataMessageConsumer implements MessageListener{

    @Autowired
    RestHighLevelClient restHighLevelClient;
    public void onMessage(Message message) {
        String skuIdsString = new String(message.getBody());
        List<String> skuIds = JSON.parseObject(skuIdsString,List.class);
        delete(skuIds);
    }

    private void delete(List<String> skuIds)  {
        BulkRequest bulkRequest = new BulkRequest();
        for(String id:skuIds){
            DeleteRequest deleteRequest = new DeleteRequest("sku","doc",id);
            bulkRequest.add(deleteRequest);
        }

        BulkResponse bulkResponse = null;
        try {
            bulkResponse = restHighLevelClient.bulk(bulkRequest);
            System.out.println("deleteStatus:"+bulkResponse.status());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                restHighLevelClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

}
