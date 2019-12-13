package com.taotao.service.system;

import com.taotao.entity.PageResult;
import com.taotao.pojo.system.Admin;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * admin业务逻辑层
 */
public interface AdminService {


    List<Admin> findAll();


    PageResult<Admin> findPage(int page, int size);


    List<Admin> findList(Map<String, Object> searchMap);


    PageResult<Admin> findPage(Map<String, Object> searchMap, int page, int size);


    Admin findById(Integer id);

    void add(Map<String, Object> pojo);


    void update(Map<String, Object> pojo);

    void updateIncludeRole(Map<String,Object> pojo);

    void delete(Integer id);

    Date lastLoginTime(String userName);

    PageResult<Admin> findByRoleId(Integer roleId, int page, int size);

    Map<String,Object> loadAdminById(Integer id);

    boolean confirmIsExistLoginName(String name);

    List<String> findResKeysByLoginName(String loginName);

    void addData();
}
