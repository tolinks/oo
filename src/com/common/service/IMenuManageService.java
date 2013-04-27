package com.common.service;

import java.util.List;

import com.common.model.Menu;
import com.common.tool.page.Page;
import com.common.tool.query.PropertyFilter;


/**
 * @className:IMenuManageService.java
 * @classDescription:菜单管理接口
 * @author:xiayingjie
 * @createTime:2010-7-8
 */
public interface IMenuManageService extends ServiceInterface<Menu>{
	/**
	 * 查询所有的菜单
	 * @param page
	 * @param pfList 查询条件
	 * @return
	 */
	public Page findAllMenus(Page page,List<PropertyFilter> pfList);
	
	/**
	 * 查询所有的菜单
	 * @return
	 */
	public List<Menu> findAllMenus();
}
