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

}
