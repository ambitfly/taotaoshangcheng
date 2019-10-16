package com.taotao.service.order;
import com.taotao.entity.PageResult;
import com.taotao.pojo.order.Order;
import com.taotao.pojo.order.OrderOrderItem;

import java.util.*;

/**
 * order业务逻辑层
 */
public interface OrderService {


    public List<Order> findAll();


    public PageResult<Order> findPage(int page, int size);


    public List<Order> findList(Map<String,Object> searchMap);


    public PageResult<Order> findPage(Map<String,Object> searchMap,int page, int size);


    public Order findById(String id);

    public void add(Order order);


    public void update(Order order);


    public void delete(String id);

    public OrderOrderItem findOrderOrderItemById(String id);

    public List<Order> findByIds(String[] ids);

    public void deliveryMany(List<Order> orders);

    public void orderTimeOutLogic();

}
