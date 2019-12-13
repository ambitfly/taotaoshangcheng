package com.taotao.service.order;

import com.taotao.entity.PageResult;
import com.taotao.pojo.order.ReturnCause;

import java.util.List;
import java.util.Map;

/**
 * returnCause业务逻辑层
 */
public interface ReturnCauseService {


    List<ReturnCause> findAll();


    PageResult<ReturnCause> findPage(int page, int size);


    List<ReturnCause> findList(Map<String, Object> searchMap);


    PageResult<ReturnCause> findPage(Map<String, Object> searchMap, int page, int size);


    ReturnCause findById(Integer id);

    void add(ReturnCause returnCause);


    void update(ReturnCause returnCause);


    void delete(Integer id);

}
