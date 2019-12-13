package com.taotao.service.order;

import com.taotao.entity.PageResult;
import com.taotao.pojo.order.OrderConfig;

import java.util.List;
import java.util.Map;

/**
 * orderConfig业务逻辑层
 */
public interface OrderConfigService {


    List<OrderConfig> findAll();


    PageResult<OrderConfig> findPage(int page, int size);


    List<OrderConfig> findList(Map<String, Object> searchMap);


    PageResult<OrderConfig> findPage(Map<String, Object> searchMap, int page, int size);


    OrderConfig findById(Integer id);

    void add(OrderConfig orderConfig);


    void update(OrderConfig orderConfig);


    void delete(Integer id);

}
