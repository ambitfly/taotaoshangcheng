package com.taotao.service.order;

import com.taotao.entity.PageResult;
import com.taotao.pojo.order.OrderLog;

import java.util.List;
import java.util.Map;

/**
 * orderLog业务逻辑层
 */
public interface OrderLogService {


    List<OrderLog> findAll();


    PageResult<OrderLog> findPage(int page, int size);


    List<OrderLog> findList(Map<String, Object> searchMap);


    PageResult<OrderLog> findPage(Map<String, Object> searchMap, int page, int size);


    OrderLog findById(String id);

    void add(OrderLog orderLog);


    void update(OrderLog orderLog);


    void delete(String id);

}
