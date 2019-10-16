package com.taotao.service.impl;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.taotao.dao.AdminImageMapper;
import com.taotao.dao.AdminMapper;
import com.taotao.entity.PageResult;
import com.taotao.pojo.system.Admin;
import com.taotao.pojo.system.AdminImage;
import com.taotao.service.system.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Page<Admin> admins = (Page<Admin>) adminMapper.selectByExample(example);
        return new PageResult<Admin>(admins.getTotal(),admins.getResult());
    }

    /**
     * 根据Id查询
     * @param id
     * @return
     */
    public Admin findById(Integer id) {
        return adminMapper.selectByPrimaryKey(id);
    }

    /**
     * 新增
     * @param admin
     */
    public void add(Admin admin) {
        adminMapper.insert(admin);
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
        admin.setLoginName(name);
        admin.setPassword(password);

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
        if(adminImages==null){
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

    /**
     *  删除
     * @param id
     */
    public void delete(Integer id) {
        adminMapper.deleteByPrimaryKey(id);
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

}
