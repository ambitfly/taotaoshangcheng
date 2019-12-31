package com.taotao.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.Config;
import com.github.wxpay.sdk.WXPayRequest;
import com.github.wxpay.sdk.WXPayUtil;
import com.taotao.service.order.OrderService;
import com.taotao.service.order.WxPayService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

@Service
public class WxPayServiceImpl implements WxPayService{

    @Autowired
    private Config config;
    @Override
    public Map createNative(String orderId, Integer money, String notifyUrl) {
        Map m = new HashMap();
        try {
            //1.封装请求参数
            Map<String, String> map = new HashMap();

            map.put("appid", config.getAppID());
            map.put("mch_id", config.getMchID());
            map.put("nonce_str", WXPayUtil.generateNonceStr());
            map.put("body", "涛涛");
            map.put("out_trade_no", orderId);
            map.put("total_fee", "" + money);
            map.put("spbill_create_ip", "127.0.0.1");
            map.put("notify_url", notifyUrl);
            map.put("trade_type", "NATIVE");

            String xmlParam = WXPayUtil.generateSignedXml(map, config.getKey());

            //2.发送请求
            WXPayRequest wxPayRequest = new WXPayRequest(config);
            String xmlResult = wxPayRequest.requestWithCert("/pay/unifiedorder", null, xmlParam, false);
            //System.out.println(xmlResult);
            //3.解析返回结果
            Map<String, String> mapResult = WXPayUtil.xmlToMap(xmlResult);


            m.put("code_url", mapResult.get("code_url"));
            m.put("total_fee", money + "");
            m.put("out_trade_no", orderId);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return m;
    }
    @Autowired
    private OrderService orderService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Override
    public void notifyLogic(String xml) {
        try {
            Map<String,String> map = WXPayUtil.xmlToMap(xml);
            boolean isSignatureValid = WXPayUtil.isSignatureValid(map,config.getKey());
            /*System.out.println("签名知否正确："+isSignatureValid);
            System.out.println(map.get("out_trade_no"));
            System.out.println(map.get("result_code"));*/
            if(isSignatureValid){
                if("SUCCESS".equals(map.get("result_code"))){
                    orderService.updatePayStatus(map.get("out_trade_no"),map.get("transaction_id"));

                    rabbitTemplate.convertAndSend("paynotify","",map.get("out_trade_no"));
                }else {
                    //记录日志
                }
            }else {
                //记录日志
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
