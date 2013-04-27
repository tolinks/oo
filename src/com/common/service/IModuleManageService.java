package com.common.service;

import java.util.List;

import com.common.model.Module;
import com.common.tool.page.Page;
import com.common.tool.query.PropertyFilter;

/**
 * @className:IModuleManageService.java
 * @classDescription:模块管理接口
 * @author:xiayingjie
 * @createTime:2010-7-8
 */
public interface IModuleManageService extends ServiceInterface<Module>{

	/**
	 * 查询所有的模块
	 * @param page
	 * @param pfList 查询条件
	 * @return
	 */
	public Page findAllModules(Page page,List<PropertyFilter> pfList);

	/**
	 * 查询所有的模块
	 * @return
	 */
	public List<Module> findAllMoudles();
}
