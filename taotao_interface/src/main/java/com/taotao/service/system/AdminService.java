package com.taotao.service.system;
import com.taotao.entity.PageResult;
import com.taotao.pojo.system.Admin;

import java.util.*;

/**
 * admin业务逻辑层
 */
public interface AdminService {


    public List<Admin> findAll();


    public PageResult<Admin> findPage(int page, int size);


    public List<Admin> findList(Map<String,Object> searchMap);


    public PageResult<Admin> findPage(Map<String,Object> searchMap,int page, int size);


    public Admin findById(Integer id);

    public void add(Map<String,Object> pojo);


    public void update(Map<String,Object> pojo);

    void updateIncludeRole(Map<String,Object> pojo);
    public void delete(Integer id);

    public Date lastLoginTime(String userName);

    public PageResult<Admin> findByRoleId(Integer roleId,int page, int size);

    Map<String,Object> loadAdminById(Integer id);
    public boolean confirmIsExistLoginName(String name);

    public List<String> findResKeysByLoginName(String loginName);

    public void addData();
}
