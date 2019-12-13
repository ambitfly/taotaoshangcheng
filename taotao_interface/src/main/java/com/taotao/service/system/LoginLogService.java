package com.taotao.service.system;

import com.taotao.entity.PageResult;
import com.taotao.pojo.system.LoginLog;

import java.util.List;
import java.util.Map;

/**
 * loginLog业务逻辑层
 */
public interface LoginLogService {


    List<LoginLog> findAll();


    PageResult<LoginLog> findPage(int page, int size);


    List<LoginLog> findList(Map<String, Object> searchMap);


    PageResult<LoginLog> findPage(Map<String, Object> searchMap, int page, int size);


    LoginLog findById(Integer id);

    void add(LoginLog loginLog);


    void update(LoginLog loginLog);


    void delete(Integer id);

}
