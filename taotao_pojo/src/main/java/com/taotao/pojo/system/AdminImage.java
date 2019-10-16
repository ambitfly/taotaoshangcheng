package com.taotao.pojo.system;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name="tb_admin_image")
public class AdminImage implements Serializable{
    @Id
    private Integer id;
    private Integer adminId;
    private String image;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "AdminImage{" +
                "id=" + id +
                ", adminId=" + adminId +
                ", image='" + image + '\'' +
                '}';
    }
}
