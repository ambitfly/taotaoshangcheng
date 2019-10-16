package com.taotao.service.order;
import com.taotao.entity.PageResult;
import com.taotao.pojo.order.ReturnOrderItem;

import java.util.*;

/**
 * returnOrderItem业务逻辑层
 */
public interface ReturnOrderItemService {


    public List<ReturnOrderItem> findAll();


    public PageResult<ReturnOrderItem> findPage(int page, int size);


    public List<ReturnOrderItem> findList(Map<String,Object> searchMap);


    public PageResult<ReturnOrderItem> findPage(Map<String,Object> searchMap,int page, int size);


    public ReturnOrderItem findById(Long id);

    public void add(ReturnOrderItem returnOrderItem);


    public void update(ReturnOrderItem returnOrderItem);


    public void delete(Long id);

}
