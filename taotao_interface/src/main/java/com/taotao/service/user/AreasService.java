package com.taotao.service.user;

import com.taotao.entity.PageResult;
import com.taotao.pojo.user.Areas;

import java.util.List;
import java.util.Map;

/**
 * areas业务逻辑层
 */
public interface AreasService {


    List<Areas> findAll();


    PageResult<Areas> findPage(int page, int size);


    List<Areas> findList(Map<String, Object> searchMap);


    PageResult<Areas> findPage(Map<String, Object> searchMap, int page, int size);


    Areas findById(String areaid);

    void add(Areas areas);


    void update(Areas areas);


    void delete(String areaid);

}
