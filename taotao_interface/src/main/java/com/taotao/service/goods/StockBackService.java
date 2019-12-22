package com.taotao.service.goods;

import com.taotao.pojo.order.OrderItem;

import java.util.List;

public interface StockBackService {
    /**
     * 添加待回滚列表
     * @param orderItems
     */
    void addList(List<OrderItem> orderItems);

    /**
     * 执行回滚
     */
    void doBack();
}
