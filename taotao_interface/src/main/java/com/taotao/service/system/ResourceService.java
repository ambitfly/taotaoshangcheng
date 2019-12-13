package com.taotao.service.system;

import com.taotao.entity.PageResult;
import com.taotao.pojo.system.Resource;

import java.util.List;
import java.util.Map;

/**
 * resource业务逻辑层
 */
public interface ResourceService {


    List<Resource> findAll();


    PageResult<Resource> findPage(int page, int size);


    List<Resource> findList(Map<String, Object> searchMap);


    PageResult<Resource> findPage(Map<String, Object> searchMap, int page, int size);


    Resource findById(Integer id);

    void add(Resource resource);


    void update(Resource resource);


    void delete(Integer id);

    List<Map<String, Object>> listResource();

}
