package com.common.service;

import java.util.List;

import com.common.model.UserInfo;
import com.common.tool.page.Page;
import com.common.tool.query.PropertyFilter;

/**
 * @className:IUserManageService.java
 * @classDescription:用户管理接口
 * @author:xiayingjie
 * @createTime:2010-7-8
 */
public interface IUserManageService extends ServiceInterface<UserInfo>{
	/**
	 * 根据用户名密码查找对象
	 */
	public UserInfo login(String userName,String password);
	/**
	 * 查询所有的用户
	 * @param page
	 * @param pfList 查询条件
	 * @return
	 */
	public Page findAllUsers(Page page,List<PropertyFilter> pfList);
	/**
	 * 检测用户名是否存在
	 * @param userName
	 * @return
	 */
	public boolean checkUserName(String userName);

}
