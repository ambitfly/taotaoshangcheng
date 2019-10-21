package com.taotao.pojo.system;

import javax.persistence.Table;
import java.io.Serializable;

@Table(name="tb_resource_menu")
public class ResourceMenu implements Serializable{
    private Integer resourceId;
    private String menuId;

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
}
