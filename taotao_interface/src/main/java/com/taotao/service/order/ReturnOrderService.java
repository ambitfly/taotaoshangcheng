package com.taotao.service.order;

import com.taotao.entity.PageResult;
import com.taotao.pojo.order.ReturnOrder;

import java.util.List;
import java.util.Map;

/**
 * returnOrder业务逻辑层
 */
public interface ReturnOrderService {


    List<ReturnOrder> findAll();


    PageResult<ReturnOrder> findPage(int page, int size);


    List<ReturnOrder> findList(Map<String, Object> searchMap);


    PageResult<ReturnOrder> findPage(Map<String, Object> searchMap, int page, int size);


    ReturnOrder findById(Long id);

    void add(ReturnOrder returnOrder);


    void update(ReturnOrder returnOrder);


    void delete(Long id);

}
