package com.common.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;



/**
 * @className:Role.java
 * @classDescription:角色类
 * @author:xiayingjie
 * @createTime:2010-7-5
 */
@Entity
@Table(name = "role")
public class Role implements java.io.Serializable {

    // Fields
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id",unique=true)
    private Integer id;
	@Column(name="roleName",length=50,nullable=false)
	private String roleName;//角色名称
	@Column(name="roleInfo",length=1000)
	private String roleInfo;//角色介绍
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="createTime",nullable=false)
	private Date createTime;//创建时间

	@ManyToMany(cascade={CascadeType.MERGE,CascadeType.REFRESH},fetch=FetchType.LAZY)
	@JoinTable(name="roleaction",inverseJoinColumns={@JoinColumn(name="actionId")},joinColumns={@JoinColumn(name="roleId")})
    private Set<Action> actions = new HashSet<Action>(0);
	
	@ManyToMany(cascade={CascadeType.MERGE,CascadeType.REFRESH},fetch=FetchType.LAZY)
	@JoinTable(name="userrole",inverseJoinColumns={@JoinColumn(name="userId")},joinColumns={@JoinColumn(name="roleId")})
	private Set<UserInfo> users=new HashSet(0);
	
	@ManyToMany(cascade={CascadeType.MERGE,CascadeType.REFRESH},fetch=FetchType.LAZY)
	@JoinTable(name="rolemenu",inverseJoinColumns={@JoinColumn(name="menuId")},joinColumns={@JoinColumn(name="roleId")})
	private Set<Menu> menus=new HashSet(0);

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}



	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the roleName
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * @param roleName the roleName to set
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * @return the roleInfo
	 */
	public String getRoleInfo() {
		return roleInfo;
	}

	/**
	 * @param roleInfo the roleInfo to set
	 */
	public void setRoleInfo(String roleInfo) {
		this.roleInfo = roleInfo;
	}





	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the actions
	 */
	public Set<Action> getActions() {
		return actions;
	}

	/**
	 * @param actions the actions to set
	 */
	public void setActions(Set<Action> actions) {
		this.actions = actions;
	}

	/**
	 * @return the users
	 */
	public Set<UserInfo> getUsers() {
		return users;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(Set<UserInfo> users) {
		this.users = users;
	}

	/**
	 * @return the menus
	 */
	public Set<Menu> getMenus() {
		return menus;
	}

	/**
	 * @param menus the menus to set
	 */
	public void setMenus(Set<Menu> menus) {
		this.menus = menus;
	}

}