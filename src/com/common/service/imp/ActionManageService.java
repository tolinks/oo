package com.common.service.imp;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.common.dao.DAOInterface;
import com.common.model.Action;
import com.common.model.Role;
import com.common.model.UserInfo;
import com.common.service.BaseService;
import com.common.service.IActionManageService;
import com.common.service.IUserManageService;
import com.common.tool.page.Page;
import com.common.tool.query.PropertyFilter;
import com.common.tool.query.QueryUtil;

/**
 * @className:ActionManageService.java
 * @classDescription:动作权限管理类
 * @author:xiayingjie
 * @createTime:2010-7-8
 */
@Service
public class ActionManageService extends BaseService<Action> implements IActionManageService{
	@Resource(name="actionDAO")
	private  DAOInterface<Action> actionDAO;
	
	
	//====方法定义区====//
	/**
	 * 查询所有的权限
	 * @param page
	 * @param pfList 查询条件
	 * @return
	 */
	public Page findAllActions(Page page,List<PropertyFilter> pfList){
		//初始化hql
		StringBuffer hql=new StringBuffer("from Action");
		//设置查询条件
		String condition= QueryUtil.toSqlString(pfList, true);
		hql.append(condition);
		//查找所有用户的总条数
		int totalCount=this.actionDAO.findCountBySql(hql.toString());
		page.setTotalCount(totalCount);
		
		
		//是否存在排序
		if(page.isOrderBySetted()){
			hql.append(page.getOrder());
		}
		List<Action> list=this.actionDAO.findList(hql.toString(), page.getPageNo(), page.getPageSize());
		//查出结果集
		page.setResult(list);
	
		return page;
	}
	

	
	
	
	
	//==========//	

	@Override
	protected DAOInterface<Action> getDAO() {
		return actionDAO;
	}

	/**
	 * @return the actionDAO
	 */
	public DAOInterface<Action> getActionDAO() {
		return actionDAO;
	}
	/**
	 * @param actionDAO the actionDAO to set
	 */
	public void setActionDAO(DAOInterface<Action> actionDAO) {
		this.actionDAO = actionDAO;
	}


}
