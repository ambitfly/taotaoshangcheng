package com.taotao.service.system;

import com.taotao.entity.PageResult;
import com.taotao.pojo.system.Menu;

import java.util.List;
import java.util.Map;

/**
 * menu业务逻辑层
 */
public interface MenuService {


    List<Menu> findAll();


    PageResult<Menu> findPage(int page, int size);


    List<Menu> findList(Map<String, Object> searchMap);


    PageResult<Menu> findPage(Map<String, Object> searchMap, int page, int size);


    Menu findById(String id);

    void add(Menu menu);


    void update(Menu menu);


    void delete(String id);

    List<Map<String, Object>> findAllMenu();

    List<Map<String, Object>> findMenu(String loginName);

}
