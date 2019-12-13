package com.taotao.service.order;

import com.taotao.entity.PageResult;
import com.taotao.pojo.order.OrderItem;

import java.util.List;
import java.util.Map;

/**
 * orderItem业务逻辑层
 */
public interface OrderItemService {


    List<OrderItem> findAll();


    PageResult<OrderItem> findPage(int page, int size);


    List<OrderItem> findList(Map<String, Object> searchMap);


    PageResult<OrderItem> findPage(Map<String, Object> searchMap, int page, int size);


    OrderItem findById(String id);

    void add(OrderItem orderItem);


    void update(OrderItem orderItem);


    void delete(String id);

}
