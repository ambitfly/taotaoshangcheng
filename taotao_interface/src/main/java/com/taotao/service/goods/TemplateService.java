package com.taotao.service.goods;

import com.taotao.entity.PageResult;
import com.taotao.pojo.goods.Template;

import java.util.List;
import java.util.Map;

/**
 * template业务逻辑层
 */
public interface TemplateService {


    List<Template> findAll();


    PageResult<Template> findPage(int page, int size);


    List<Template> findList(Map<String, Object> searchMap);


    PageResult<Template> findPage(Map<String, Object> searchMap, int page, int size);


    Template findById(Integer id);

    void add(Template template);


    void update(Template template);


    void delete(Integer id);

    Map<Integer, String> idReturnName();
}
