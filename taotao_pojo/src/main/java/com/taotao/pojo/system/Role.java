package com.taotao.pojo.system;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
/**
 * role实体类
 * @author Administrator
 *
 */
@Table(name="tb_role")
public class Role implements Serializable{

	@Id
	private Integer id;//ID


	

	private String name;//角色名称
	@Transient
	private Integer adminNumber;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Integer getAdminNumber() {
		return adminNumber;
	}

	public void setAdminNumber(Integer adminNumber) {
		this.adminNumber = adminNumber;
	}

	@Override
	public String toString() {
		return "Role{" +
				"id=" + id +
				", name='" + name + '\'' +
				", adminNumber=" + adminNumber +
				'}';
	}
}
