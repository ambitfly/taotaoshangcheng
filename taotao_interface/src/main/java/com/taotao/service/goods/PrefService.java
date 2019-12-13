package com.taotao.service.goods;

import com.taotao.entity.PageResult;
import com.taotao.pojo.goods.Pref;

import java.util.List;
import java.util.Map;

/**
 * pref业务逻辑层
 */
public interface PrefService {


    List<Pref> findAll();


    PageResult<Pref> findPage(int page, int size);


    List<Pref> findList(Map<String, Object> searchMap);


    PageResult<Pref> findPage(Map<String, Object> searchMap, int page, int size);


    Pref findById(Integer id);

    void add(Pref pref);


    void update(Pref pref);


    void delete(Integer id);

}
