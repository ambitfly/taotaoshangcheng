package com.taotao.pojo.system;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * admin实体类
 * @author Administrator
 *
 */
@Table(name="tb_admin")
public class Admin implements Serializable{

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)//主键自增
	private Integer id;//id


	

	private String loginName;//用户名

	private String password;//密码

	private String status;//状态
	@Transient
	private List<Role> roles;
	@Transient
	private Date lastLoginTime;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	@Override
	public String toString() {
		return "Admin{" +
				"id=" + id +
				", loginName='" + loginName + '\'' +
				", password='" + password + '\'' +
				", status='" + status + '\'' +
				", roles=" + roles +
				", lastLoginTime=" + lastLoginTime +
				'}';
	}
}
