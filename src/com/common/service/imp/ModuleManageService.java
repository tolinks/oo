package com.common.service.imp;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.common.dao.DAOInterface;
import com.common.model.Module;
import com.common.model.UserInfo;
import com.common.service.BaseService;
import com.common.service.IModuleManageService;
import com.common.tool.page.Page;
import com.common.tool.query.PropertyFilter;
import com.common.tool.query.QueryUtil;


/**
 * @className:ModuleManageService.java
 * @classDescription:模块管理类
 * @author:xiayingjie
 * @createTime:2010-7-8
 */
@Service
public class ModuleManageService extends BaseService<Module> implements IModuleManageService{
	@Resource(name="moduleDAO")
	private  DAOInterface<Module> moduleDAO;
	
	//====方法定义区====//
	/**
	 * 查询所有的模块
	 * @param page
	 * @param pfList 查询条件
	 * @return
	 */
	public Page findAllModules(Page page,List<PropertyFilter> pfList){
		//初始化hql
		StringBuffer hql=new StringBuffer("from Module");
		//设置查询条件
		String condition= QueryUtil.toSqlString(pfList, true);
		hql.append(condition);
		//查找所有用户的总条数
		int totalCount=this.moduleDAO.findCountBySql(hql.toString());
		page.setTotalCount(totalCount);
		
		
		//是否存在排序
		if(page.isOrderBySetted()){
			hql.append(page.getOrder());
		}
		List<Module> list=this.moduleDAO.findList(hql.toString(), page.getPageNo(), page.getPageSize());
		//查出结果集
		page.setResult(list);
	
		return page;
	}

	
	/**
	 * 查询所有的模块
	 * @return
	 */
	public List<Module> findAllMoudles(){
		//初始化hql
		String hql="from Module";
		return this.moduleDAO.findList(hql);
	}
	
	
	//==========//	
	

	@Override
	protected DAOInterface<Module> getDAO() {
		return moduleDAO;
	}

	/**
	 * @return the moduleDAO
	 */
	public DAOInterface<Module> getModuleDAO() {
		return moduleDAO;
	}

	/**
	 * @param moduleDAO the moduleDAO to set
	 */
	public void setModuleDAO(DAOInterface<Module> moduleDAO) {
		this.moduleDAO = moduleDAO;
	}

}
