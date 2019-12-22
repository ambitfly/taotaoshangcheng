package com.taotao.consumer;

import com.alibaba.fastjson.JSON;
import com.taotao.pojo.order.OrderItem;
import com.taotao.service.goods.StockBackService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BackMessageConsumer implements MessageListener {
    @Autowired
    private StockBackService stockBackService;
    public void onMessage(Message message) {

        try {
            String jsonString = new String(message.getBody());
            List<OrderItem> orderItems = JSON.parseArray(jsonString,OrderItem.class);
            stockBackService.addList(orderItems);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
