package com.taotao.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.taotao.pojo.order.Order;
import com.taotao.service.order.OrderService;
import com.taotao.service.order.WxPayService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
@RestController
@RequestMapping("/wxpay")
public class WxPayController {
    @Reference
    private OrderService orderService;
    @Reference
    private WxPayService wxPayService;

    @GetMapping("/createNative")
    public Map createNative(String orderId){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Order order = orderService.findById(orderId);
        if(order!=null){
            if("0".equals(order.getOrderStatus())&&"0".equals(order.getPayStatus())&&username.equals(order.getUsername())){
               return wxPayService.createNative(orderId,order.getPayMoney(),"http://taotao.easy.echosite.cn/wxpay/notify.do");
            }else {
                return null;
            }
        }else {
            return null;
        }
    }

    @RequestMapping("/notify")
    public void  notifyLogic(HttpServletRequest request){
        InputStream inputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            inputStream = request.getInputStream();
            byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while((len=inputStream.read(buffer))!=-1){
                byteArrayOutputStream.write(buffer,0,len);
            }
            String result = new String(byteArrayOutputStream.toByteArray());
            //System.out.println(result);
            wxPayService.notifyLogic(result);

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(byteArrayOutputStream!=null) byteArrayOutputStream.close();
                if(inputStream!=null) inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("支付成功回调！");
    }
}
