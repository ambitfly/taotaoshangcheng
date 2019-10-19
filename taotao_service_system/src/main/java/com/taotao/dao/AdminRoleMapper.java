package com.taotao.dao;

import com.taotao.pojo.system.AdminRole;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface AdminRoleMapper extends Mapper<AdminRole> {
    @Select("SELECT COUNT(admin_id) FROM tb_admin_role WHERE role_id = #{roleId}")
    Integer countAdmin(@Param("roleId") Integer roleId);
}
