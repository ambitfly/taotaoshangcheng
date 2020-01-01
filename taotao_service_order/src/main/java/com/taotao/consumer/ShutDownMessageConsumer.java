package com.taotao.consumer;

import com.taotao.pojo.order.Order;
import com.taotao.service.order.OrderService;
import com.taotao.service.order.WxPayService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;
public class ShutDownMessageConsumer implements MessageListener{
    @Autowired
    private OrderService orderService;
    @Autowired
    private WxPayService wxPayService;
    @Override
    public void onMessage(Message message) {
        String orderId = new String(message.getBody());
        Order order = orderService.findById(orderId);
        if("0".equals(order.getOrderStatus())){
            Map map = wxPayService.findNative(orderId);
            if("ORDERNOTEXIST".equals(map.get("err_code"))){
                orderService.shutDownOrder(orderId);
                return;
            }
            String tradeState = (String)map.get("trade_state");
            if("NOTPAY".equals(tradeState)){
                orderService.shutDownOrder(orderId);
            }else if ("SUCCESS".equals(tradeState)){
                //补偿操作

            }
        }

    }
}
