package com.taotao.service.goods;

import com.taotao.entity.PageResult;
import com.taotao.pojo.goods.Spec;

import java.util.List;
import java.util.Map;

/**
 * spec业务逻辑层
 */
public interface SpecService {


    List<Spec> findAll();


    PageResult<Spec> findPage(int page, int size);


    List<Spec> findList(Map<String, Object> searchMap);


    PageResult<Spec> findPage(Map<String, Object> searchMap, int page, int size);


    Spec findById(Integer id);

    void add(Spec spec);


    void update(Spec spec);


    void delete(Integer id);

    List<Map> findSpecByTemplateId(Integer templateId);

}
