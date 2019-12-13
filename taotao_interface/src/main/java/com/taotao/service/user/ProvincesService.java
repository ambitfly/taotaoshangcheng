package com.taotao.service.user;

import com.taotao.entity.PageResult;
import com.taotao.pojo.user.Provinces;

import java.util.List;
import java.util.Map;

/**
 * provinces业务逻辑层
 */
public interface ProvincesService {


    List<Provinces> findAll();


    PageResult<Provinces> findPage(int page, int size);


    List<Provinces> findList(Map<String, Object> searchMap);


    PageResult<Provinces> findPage(Map<String, Object> searchMap, int page, int size);


    Provinces findById(String provinceid);

    void add(Provinces provinces);


    void update(Provinces provinces);


    void delete(String provinceid);

}
