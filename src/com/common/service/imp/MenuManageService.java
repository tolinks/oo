package com.common.service.imp;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.common.dao.DAOInterface;
import com.common.model.Action;
import com.common.model.Menu;
import com.common.service.BaseService;
import com.common.service.IMenuManageService;
import com.common.tool.page.Page;
import com.common.tool.query.PropertyFilter;
import com.common.tool.query.QueryUtil;


/**
 * @className:MenuManageService.java
 * @classDescription:菜单管理类
 * @author:xiayingjie
 * @createTime:2010-7-8
 */
@Service
public class MenuManageService extends BaseService<Menu> implements IMenuManageService{
	@Resource(name="menuDAO")
	private  DAOInterface<Menu> menuDAO;
	
	//====方法定义区====//
	/**
	 * 查询所有的权限
	 * @param page
	 * @param pfList 查询条件
	 * @return
	 */
	public Page findAllMenus(Page page,List<PropertyFilter> pfList){
		//初始化hql
		StringBuffer hql=new StringBuffer("from Menu");
		//设置查询条件
		String condition= QueryUtil.toSqlString(pfList, true);
		hql.append(condition);
		//查找所有用户的总条数
		int totalCount=this.menuDAO.findCountBySql(hql.toString());
		page.setTotalCount(totalCount);
		
		
		//是否存在排序
		if(page.isOrderBySetted()){
			hql.append(page.getOrder());
		}
		List<Menu> list=this.menuDAO.findList(hql.toString(), page.getPageNo(), page.getPageSize());
		//查出结果集
		page.setResult(list);
	
		return page;
	}
	/**
	 * 查询所有的菜单
	 * @return
	 */
	public List<Menu> findAllMenus(){
		String hql="from Menu order by orders";
		return this.menuDAO.findList(hql);
	}

	
	
	
	
	//==========//	


	@Override
	protected DAOInterface<Menu> getDAO() {
		return menuDAO;
	}

	/**
	 * @return the menuDAO
	 */
	public DAOInterface<Menu> getMenuDAO() {
		return menuDAO;
	}

	/**
	 * @param menuDAO the menuDAO to set
	 */
	public void setMenuDAO(DAOInterface<Menu> menuDAO) {
		this.menuDAO = menuDAO;
	}



}
