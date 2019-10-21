package com.taotao.dao;

import com.taotao.pojo.system.Menu;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface MenuMapper extends Mapper<Menu> {

    @Select("SELECT menu_id FROM tb_resource_menu WHERE resource_id IN " +
            "(SELECT resource_id FROM tb_role_resource WHERE role_id IN " +
            "(SELECT role_id  FROM tb_admin_role WHERE admin_id IN " +
            "(SELECT id FROM tb_admin WHERE login_name = 'admin')))")
    List<String> findMenuIdByLoginName(@Param("loginName") String loginName);

    @Select("SELECT * FROM tb_menu WHERE id IN " +
            "(SELECT menu_id FROM tb_resource_menu WHERE resource_id IN " +
            "(SELECT resource_id FROM tb_role_resource WHERE role_id IN " +
            "(SELECT role_id  FROM tb_admin_role WHERE admin_id IN " +
            "(SELECT id FROM tb_admin WHERE login_name = #{loginName}))))")
    List<Menu> findMenuByLoginName(@Param("loginName") String loginName);
}
