package com.taotao.service.order;

import com.taotao.entity.PageResult;
import com.taotao.pojo.order.Preferential;

import java.util.List;
import java.util.Map;

/**
 * preferential业务逻辑层
 */
public interface PreferentialService {


    List<Preferential> findAll();


    PageResult<Preferential> findPage(int page, int size);


    List<Preferential> findList(Map<String, Object> searchMap);


    PageResult<Preferential> findPage(Map<String, Object> searchMap, int page, int size);


    Preferential findById(Integer id);

    void add(Preferential preferential);


    void update(Preferential preferential);


    void delete(Integer id);

}
