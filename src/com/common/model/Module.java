package com.common.model;

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
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;




/**
 * @className:Module.java
 * @classDescription:模块对象
 * @author:xiayingjie
 * @createTime:2010-7-5
 */
@Entity
@Table(name = "module")
public class Module implements java.io.Serializable {

	// Fields
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id",unique=true)
	private Integer id;
	@Column(name="moduleName",nullable=false)
	private String moduleName;//模块名称
	@Column(name="moduleInfo")
	private String moduleInfo;//模块介绍
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="createTime",nullable=false)
	private Date createTime;//注册时间
	@OneToMany(
			   cascade=CascadeType.ALL,
			   fetch=FetchType.LAZY,
			   mappedBy="module"		   
	)
	@OrderBy("orders")
	private Set<Action> actions = new HashSet<Action>(0);
	
	
	/**
	 * @return the moduleInfo
	 */
	public String getModuleInfo() {
		return moduleInfo;
	}
	/**
	 * @param moduleInfo the moduleInfo to set
	 */
	public void setModuleInfo(String moduleInfo) {
		this.moduleInfo = moduleInfo;
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
	 * @return the moduleName
	 */
	public String getModuleName() {
		return moduleName;
	}
	/**
	 * @param moduleName the moduleName to set
	 */
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
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

	


}