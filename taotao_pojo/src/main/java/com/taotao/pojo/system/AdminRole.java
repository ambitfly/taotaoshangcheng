package com.taotao.pojo.system;

import javax.persistence.Table;
import java.io.Serializable;

@Table(name="tb_admin_role")
public class AdminRole implements Serializable{
    private Integer adminId;
    private Integer roleId;

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "AdminRole{" +
                "adminId=" + adminId +
                ", roleId=" + roleId +
                '}';
    }
}
