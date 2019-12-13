package com.taotao.service.goods;

import com.taotao.entity.PageResult;
import com.taotao.pojo.goods.Para;

import java.util.List;
import java.util.Map;

/**
 * para业务逻辑层
 */
public interface ParaService {


    List<Para> findAll();


    PageResult<Para> findPage(int page, int size);


    List<Para> findList(Map<String, Object> searchMap);


    PageResult<Para> findPage(Map<String, Object> searchMap, int page, int size);


    Para findById(Integer id);

    void add(Para para);


    void update(Para para);


    void delete(Integer id);

}
