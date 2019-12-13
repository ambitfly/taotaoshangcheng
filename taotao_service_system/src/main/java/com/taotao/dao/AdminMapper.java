package com.taotao.dao;

import com.taotao.pojo.system.Admin;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface AdminMapper extends Mapper<Admin> {
    @Select("SELECT id,login_name,`password`,`status` FROM tb_admin WHERE id IN " +
            "(SELECT admin_id FROM tb_admin_role WHERE role_id = #{roleId} )")
    List<Admin> findByRoleId(@Param("roleId") Integer roleId);

    @Select("SELECT res_key FROM tb_resource WHERE id IN " +
                "(SELECT resource_id FROM tb_role_resource WHERE role_id IN " +
                     "(SELECT role_id  FROM tb_admin_role WHERE admin_id IN " +
                            "(SELECT id FROM tb_admin WHERE login_name = #{loginName})))")
    List<String> findResKeysByLoginName(@Param("loginName") String loginName);



}
