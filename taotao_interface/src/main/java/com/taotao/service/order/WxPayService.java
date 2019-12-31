package com.taotao.service.order;

import java.util.Map;
public interface WxPayService {
    /**
     * 生成微信支付二维码
     * @param orderId  订单号
     * @param money  金额（分）
     * @param notifyUrl 回调地址
     * @return
     */
    public Map createNative(String orderId,Integer money,String notifyUrl);

    /**
     * 微信支付回调
     * @param xml
     */
    public void notifyLogic(String xml);
}
