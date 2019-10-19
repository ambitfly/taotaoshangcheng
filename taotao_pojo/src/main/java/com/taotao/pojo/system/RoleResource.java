package com.taotao.pojo.system;

import org.omg.PortableInterceptor.INACTIVE;

import javax.persistence.Table;
import java.io.Serializable;

@Table(name="tb_role_resource")
public class RoleResource implements Serializable {
   private Integer roleId;
   private Integer resourceId;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    @Override
    public String toString() {
        return "RoleResource{" +
                "roleId=" + roleId +
                ", resourceId=" + resourceId +
                '}';
    }
}
