package com.taotao.service.config;

import com.taotao.entity.PageResult;
import com.taotao.pojo.config.FreightTemplate;

import java.util.List;
import java.util.Map;

/**
 * freightTemplate业务逻辑层
 */
public interface FreightTemplateService {


    List<FreightTemplate> findAll();


    PageResult<FreightTemplate> findPage(int page, int size);


    List<FreightTemplate> findList(Map<String, Object> searchMap);


    PageResult<FreightTemplate> findPage(Map<String, Object> searchMap, int page, int size);


    FreightTemplate findById(Integer id);

    void add(FreightTemplate freightTemplate);


    void update(FreightTemplate freightTemplate);


    void delete(Integer id);

}
