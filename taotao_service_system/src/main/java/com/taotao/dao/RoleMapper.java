package com.taotao.dao;

import com.taotao.pojo.system.Role;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


public interface RoleMapper extends Mapper<Role> {
    @Select("SELECT * FROM tb_role WHERE id IN " +
            " (SELECT role_id FROM tb_admin_role WHERE admin_id = #{adminId})")
    List<Role> getRoles(@Param("adminId") Integer adminId);
}
