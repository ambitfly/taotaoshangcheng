package com.taotao.service.business;

import com.taotao.entity.PageResult;
import com.taotao.pojo.business.Activity;

import java.util.List;
import java.util.Map;

/**
 * activity业务逻辑层
 */
public interface ActivityService {


    List<Activity> findAll();


    PageResult<Activity> findPage(int page, int size);


    List<Activity> findList(Map<String, Object> searchMap);


    PageResult<Activity> findPage(Map<String, Object> searchMap, int page, int size);


    Activity findById(Integer id);

    void add(Activity activity);


    void update(Activity activity);


    void delete(Integer id);

}
