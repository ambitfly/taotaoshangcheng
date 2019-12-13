package com.taotao.service.order;

import com.taotao.entity.PageResult;
import com.taotao.pojo.order.ReturnOrderItem;

import java.util.List;
import java.util.Map;

/**
 * returnOrderItem业务逻辑层
 */
public interface ReturnOrderItemService {


    List<ReturnOrderItem> findAll();


    PageResult<ReturnOrderItem> findPage(int page, int size);


    List<ReturnOrderItem> findList(Map<String, Object> searchMap);


    PageResult<ReturnOrderItem> findPage(Map<String, Object> searchMap, int page, int size);


    ReturnOrderItem findById(Long id);

    void add(ReturnOrderItem returnOrderItem);


    void update(ReturnOrderItem returnOrderItem);


    void delete(Long id);

}
