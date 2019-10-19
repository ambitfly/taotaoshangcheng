package com.taotao.dao;

import com.taotao.pojo.system.LoginLog;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;

public interface LoginLogMapper extends Mapper<LoginLog> {
    @Select("SELECT login_time FROM tb_login_log WHERE login_name= #{loginName} ORDER BY login_time DESC LIMIT 1")
    public Date findLastTimeLogin(@Param("loginName") String loginName);
}
