package com.taotao.service.system;

import com.taotao.entity.PageResult;
import com.taotao.pojo.system.Role;

import java.util.List;
import java.util.Map;

/**
 * role业务逻辑层
 */
public interface RoleService {


    List<Role> findAll();


    PageResult<Role> findPage(int page, int size);


    List<Role> findList(Map<String, Object> searchMap);


    PageResult<Role> findPage(Map<String, Object> searchMap, int page, int size);


    Role findById(Integer id);

    void add(Role role);


    void update(Role role);


    void delete(Integer id);

    void saveRoleResource(Integer roleId, List<Integer> menusIds);

    List<Integer> findResourceIdsByRoleId(Integer roleId);
}
