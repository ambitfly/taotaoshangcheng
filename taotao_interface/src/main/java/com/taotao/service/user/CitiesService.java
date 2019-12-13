package com.taotao.service.user;

import com.taotao.entity.PageResult;
import com.taotao.pojo.user.Cities;

import java.util.List;
import java.util.Map;

/**
 * cities业务逻辑层
 */
public interface CitiesService {


    List<Cities> findAll();


    PageResult<Cities> findPage(int page, int size);


    List<Cities> findList(Map<String, Object> searchMap);


    PageResult<Cities> findPage(Map<String, Object> searchMap, int page, int size);


    Cities findById(String cityid);

    void add(Cities cities);


    void update(Cities cities);


    void delete(String cityid);

}
