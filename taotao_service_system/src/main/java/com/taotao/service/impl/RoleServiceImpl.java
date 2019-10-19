package com.taotao.service.impl;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.taotao.dao.AdminRoleMapper;
import com.taotao.dao.RoleMapper;
import com.taotao.dao.RoleResourceMapper;
import com.taotao.entity.PageResult;
import com.taotao.pojo.system.Role;
import com.taotao.pojo.system.RoleResource;
import com.taotao.service.system.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = RoleService.class)
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    /**
     * 返回全部记录
     * @return
     */
    public List<Role> findAll() {
        return roleMapper.selectAll();
    }

    /**
     * 分页查询
     * @param page 页码
     * @param size 每页记录数
     * @return 分页结果
     */
    public PageResult<Role> findPage(int page, int size) {
        PageHelper.startPage(page,size);
        Page<Role> roles = (Page<Role>) roleMapper.selectAll();
        return new PageResult<Role>(roles.getTotal(),roles.getResult());
    }

    /**
     * 条件查询
     * @param searchMap 查询条件
     * @return
     */
    public List<Role> findList(Map<String, Object> searchMap) {
        Example example = createExample(searchMap);
        return roleMapper.selectByExample(example);
    }
    @Autowired
    AdminRoleMapper adminRoleMapper;

    /**
     * 分页+条件查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    public PageResult<Role> findPage(Map<String, Object> searchMap, int page, int size) {
        PageHelper.startPage(page,size);
        Example example = createExample(searchMap);
        List<Role> roleList = roleMapper.selectByExample(example);
        //统计成员数量
        for(Role role:roleList){
            role.setAdminNumber(adminRoleMapper.countAdmin(role.getId()));
            //System.out.println(role);
        }

        Page<Role> roles = (Page<Role>) roleList;
        return new PageResult<Role>(roles.getTotal(),roles.getResult());
    }

    /**
     * 根据Id查询
     * @param id
     * @return
     */
    public Role findById(Integer id) {
        return roleMapper.selectByPrimaryKey(id);
    }

    /**
     * 新增
     * @param role
     */
    public void add(Role role) {
        roleMapper.insert(role);
    }

    /**
     * 修改
     * @param role
     */
    public void update(Role role) {
        roleMapper.updateByPrimaryKeySelective(role);
    }

    /**
     *  删除
     * @param id
     */
    public void delete(Integer id) {
        roleMapper.deleteByPrimaryKey(id);
    }

    /**
     * 构建查询条件
     * @param searchMap
     * @return
     */
    private Example createExample(Map<String, Object> searchMap){
        Example example=new Example(Role.class);
        Example.Criteria criteria = example.createCriteria();
        if(searchMap!=null){
            // 角色名称
            if(searchMap.get("name")!=null && !"".equals(searchMap.get("name"))){
                criteria.andLike("name","%"+searchMap.get("name")+"%");
            }

            // ID
            if(searchMap.get("id")!=null ){
                criteria.andEqualTo("id",searchMap.get("id"));
            }

        }
        return example;
    }
    @Autowired
    RoleResourceMapper roleResourceMapper;
    @Override
    @Transactional
    public void saveRoleResource(Integer roleId, List<Integer> menusIds) {
        //先删除roleId的行
        Example example = new Example(RoleResource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleId",roleId);
        roleResourceMapper.deleteByExample(example);

        for(Integer resourceId:menusIds){
            RoleResource roleResource = new RoleResource();
            roleResource.setResourceId(resourceId);
            roleResource.setRoleId(roleId);
            roleResourceMapper.insert(roleResource);
        }
    }

    @Override
    public List<Integer> findResourceIdsByRoleId(Integer roleId) {
        Example example = new Example(RoleResource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleId",roleId);

        List<RoleResource> roleResources = roleResourceMapper.selectByExample(example);
        List<Integer> list = new ArrayList();
        for(RoleResource roleResource:roleResources){
            list.add(roleResource.getResourceId());
        }
        return list;
    }
}
