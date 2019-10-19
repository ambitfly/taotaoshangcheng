package com.taotao.service.impl;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.taotao.dao.*;
import com.taotao.entity.PageResult;
import com.taotao.pojo.system.*;
import com.taotao.service.system.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.persistence.Transient;
import java.util.*;

@Service(interfaceClass = AdminService.class)
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    /**
     * 返回全部记录
     * @return
     */
    public List<Admin> findAll() {
        return adminMapper.selectAll();
    }

    /**
     * 分页查询
     * @param page 页码
     * @param size 每页记录数
     * @return 分页结果
     */
    public PageResult<Admin> findPage(int page, int size) {
        PageHelper.startPage(page,size);
        Page<Admin> admins = (Page<Admin>) adminMapper.selectAll();
        return new PageResult<Admin>(admins.getTotal(),admins.getResult());
    }

    /**
     * 条件查询
     * @param searchMap 查询条件
     * @return
     */
    public List<Admin> findList(Map<String, Object> searchMap) {
        Example example = createExample(searchMap);
        return adminMapper.selectByExample(example);
    }
    @Autowired
    private RoleMapper roleMapper;
    /**
     * 分页+条件查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    public PageResult<Admin> findPage(Map<String, Object> searchMap, int page, int size) {
        PageHelper.startPage(page,size);
        Example example = createExample(searchMap);
        List<Admin> adminList = adminMapper.selectByExample(example);
        initAdmin(adminList);
        Page<Admin> admins = (Page<Admin>) adminList;
        return new PageResult<Admin>(admins.getTotal(),admins.getResult());
    }

    @Override
    public PageResult<Admin> findByRoleId(Integer roleId,int page, int size) {
        System.out.println("roleId==="+roleId);
        System.out.println("admin==="+adminMapper.findByRoleId(roleId));
        PageHelper.startPage(page,size);

        List<Admin> adminList = adminMapper.findByRoleId(roleId);
        //System.out.println("adminList==="+adminList);
        for(Admin admin:adminList){
            Admin admin1 = adminMapper.selectByPrimaryKey(admin.getId());
            admin.setLoginName(admin1.getLoginName());
        }
        initAdmin(adminList);
        System.out.println("adminList==="+adminList);

        Page<Admin> admins = (Page<Admin>) adminList;
        //System.out.println("admins============"+admins);
        return new PageResult<Admin>(admins.getTotal(),admins.getResult());
    }

    private void initAdmin(List<Admin> adminList){
        for(Admin admin:adminList){
            Date lastLoginTime = loginLogMapper.findLastTimeLogin(admin.getLoginName());
            admin.setLastLoginTime(lastLoginTime);
            List<Role> roles = roleMapper.getRoles(admin.getId());
            admin.setRoles(roles);
        }
    }
    /**
     * 根据Id查询
     * @param id
     * @return
     */
    public Admin findById(Integer id) {
        return adminMapper.selectByPrimaryKey(id);
    }
    @Autowired
    AdminRoleMapper adminRoleMapper;

    /**
     * 新增
     * @param pojo
     */

    @Transactional
    public void add(Map<String,Object> pojo) {
         /*
         * 前后端约定格式：
         * {"loginName":"xxxxx",(String)
         *  newPassword":"xxxxx",(String)
         *  "roleIds":[],(List<Integer>)
         * }
         * */
        //insert Admin
        Admin admin = new Admin();
        admin.setLoginName((String)pojo.get("loginName"));
        admin.setPassword((String)pojo.get("password"));
        admin.setStatus("0");
        adminMapper.insert(admin);

        //insert 中间表 tb_admin_role
        List<Integer> RoleIds = (List<Integer>)pojo.get("roleIds");
        for(Integer roleId:RoleIds){
            AdminRole adminRole = new AdminRole();
            adminRole.setAdminId(admin.getId());//@GeneratedValue(strategy=GenerationType.IDENTITY)该注解自动返回主键
            adminRole.setRoleId(roleId);
            adminRoleMapper.insert(adminRole);
        }


    }

    @Autowired
    AdminImageMapper adminImageMapper;
    /**
     * 修改
     * @param pojo
     */
    @Transactional
    public void update(Map<String,Object> pojo) {

        //修改admin
        Admin admin = new Admin();
        String name = (String)pojo.get("loginName");
        String password = (String)pojo.get("newPassword");
        String status = (String)pojo.get("status");
        admin.setLoginName(name);
        admin.setPassword(password);
        admin.setStatus(status);
        Map<String,Object> map = new HashMap();
        map.put("loginName",name);
        Integer adminId = findList(map).get(0).getId();
        admin.setId(adminId);
        adminMapper.updateByPrimaryKeySelective(admin);

        //修改adminImage
        Example example = new Example(AdminImage.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("adminId",adminId);
        List<AdminImage> adminImages = adminImageMapper.selectByExample(example);
        //System.out.println("==========adminImages"+adminImages);
        if(adminImages.size()==0){
            AdminImage adminImage = new AdminImage();
            adminImage.setImage((String)pojo.get("image"));
            adminImage.setAdminId(adminId);
            adminImageMapper.insert(adminImage);
        }else{
            AdminImage adminImage = adminImages.get(0);
            adminImage.setImage((String)pojo.get("image"));
            adminImageMapper.updateByPrimaryKeySelective(adminImage);
        }


    }

    @Transactional
    public void updateIncludeRole(Map<String, Object> pojo) {
        //更新用户名密码
        Integer adminId = (Integer) pojo.get("id");
        String loginName = (String)pojo.get("loginName");
        String password = (String)pojo.get("newPassword");
        String status = (String)pojo.get("status");
        List<Integer> list = (List<Integer>)pojo.get("roleIds");

        Admin admin = new Admin();
        admin.setId(adminId);
        admin.setLoginName(loginName);
        if(password!=""){
            admin.setPassword(password);
        }
        admin.setStatus(status);
        adminMapper.updateByPrimaryKeySelective(admin);

        //更新用户角色中间表
        Example example = new Example(AdminRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("adminId",adminId);
        adminRoleMapper.deleteByExample(example);
        for(Integer roleId:list){
            AdminRole adminRole = new AdminRole();
            adminRole.setAdminId(adminId);
            adminRole.setRoleId(roleId);
            adminRoleMapper.insert(adminRole);
        }

    }

    /**
     *  删除
     * @param id
     */
    @Transactional
    public void delete(Integer id) {
        //删除Admin
        adminMapper.deleteByPrimaryKey(id);
        //删除AdminRole
        Example example = new Example(AdminRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("adminId",id);
        adminRoleMapper.deleteByExample(example);
    }

    /**
     * 构建查询条件
     * @param searchMap
     * @return
     */
    private Example createExample(Map<String, Object> searchMap){
        Example example=new Example(Admin.class);
        Example.Criteria criteria = example.createCriteria();
        if(searchMap!=null){
            // 用户名
            if(searchMap.get("loginName")!=null && !"".equals(searchMap.get("loginName"))){
                criteria.andEqualTo("loginName",searchMap.get("loginName"));
            }
            // 密码
            if(searchMap.get("password")!=null && !"".equals(searchMap.get("password"))){
                criteria.andLike("password","%"+searchMap.get("password")+"%");
            }
            // 状态
            if(searchMap.get("status")!=null && !"".equals(searchMap.get("status"))){
                criteria.andEqualTo("status",searchMap.get("status"));
            }

            // id
            if(searchMap.get("id")!=null ){
                criteria.andEqualTo("id",searchMap.get("id"));
            }

        }
        return example;
    }
    @Autowired
    private LoginLogMapper loginLogMapper;

    @Override
    public Date lastLoginTime(String userName) {

        return loginLogMapper.findLastTimeLogin(userName);
    }

    @Override
    public Map<String, Object> loadAdminById(Integer id) {
        Admin admin = adminMapper.selectByPrimaryKey(id);
        Map<String,Object> map = new HashMap();
        map.put("id",id);
        map.put("loginName",admin.getLoginName());
        map.put("status",admin.getStatus());
        List<Role> roles = roleMapper.getRoles(id);
        List<Integer> list = new ArrayList();
        for(Role role:roles){
            list.add(role.getId());
        }
        map.put("roleIds",list);

        return map;
    }

    public boolean confirmIsExistLoginName(String name){
        Example example = new Example(Admin.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("loginName",name);
        List<Admin> adminList = adminMapper.selectByExample(example);
        if(adminList.size()!=0){
            return true;
        }else{
            return false;
        }
    }
}
