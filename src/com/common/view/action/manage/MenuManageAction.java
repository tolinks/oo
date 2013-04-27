package com.common.view.action.manage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.common.model.Action;
import com.common.model.Menu;
import com.common.model.Module;
import com.common.model.Role;
import com.common.model.UserInfo;
import com.common.tool.DateUtil;
import com.common.tool.Struts2Utils;
import com.common.tool.page.Condition;
import com.common.tool.page.Page;
import com.common.tool.page.PageUtil;
import com.common.tool.query.PropertyFilter;
import com.common.view.action.BaseAction;

/**
 * @className:MenuManageAction.java
 * @classDescription:角色管理Action
 * @author:xiayingjie
 * @createTime:2010-7-16
 */
@Namespace("/manage")
@Results({ 
	@Result(name = "success", location = "/manage/menu/menuManage.jsp"),
	@Result(name = BaseAction.RELOAD, location = "/manage/menu-manage.menu", type = "redirect"),
	@Result(name = "alterMenu", location = "/manage/menu/alterMenu.jsp"), 
	@Result(name = "toSaveMenu", location = "/manage/menu/addMenu.jsp"), 
	@Result(name = "error", location = "/manage/login.jsp") })
public class MenuManageAction extends BaseAction {
	

	Page<Menu> page=new Page<Menu>(15);
	
	/**
	 * 查找所有的人员
	 */
	public String list()throws Exception{
		//fileds
		String menuName=this.getParameter("menuName");
		String pageNo=this.getParameter("pageNo");
		String order=this.getParameter("order");
		String startDate=this.getParameter("startDate");
		String endDate=this.getParameter("endDate");
		
		StringBuffer condition=new StringBuffer();
		
		//查询条件
		PropertyFilter pf=new PropertyFilter("menuName:LIKE_S",menuName);
		PropertyFilter startPf=new PropertyFilter("createTime:GT_D",startDate);
		PropertyFilter endPf=new PropertyFilter("createTime:LT_D",endDate);
		List<PropertyFilter>pfList=new ArrayList();
		pfList.add(pf);
		pfList.add(startPf);
		pfList.add(endPf);
		
		//设置跳转页面
		StringBuffer forwordName=new StringBuffer(this.getRoot()+"/manage/menu-manage.action");
	

		//获取分页跳转页面
		List<Condition> fragmentList=new ArrayList();
		fragmentList.add(new Condition("menuName",menuName,"匹配'"+menuName+"'"));
		fragmentList.add(new Condition("order",order,"排序",false));
		fragmentList.add(new Condition("startDate",startDate,"大于"+startDate));
		fragmentList.add(new Condition("endDate",endDate,"小于"+endDate));

		String forwarCondition=PageUtil.getForwardCondition(fragmentList);
		forwordName.append(forwarCondition);
		//获取排序跳转页面
		String orderCondition=PageUtil.getOrderCondition(fragmentList);
		this.setRequestAttribute("order", orderCondition);
		
		//获取过滤查询集合
		List<Condition> filterList=PageUtil.getFilterConditions(fragmentList);
		this.setRequestAttribute("filterList", filterList);

		//初始化page属性值--按时间排序
		if(null==order){
			page.setOrder("createTime:asc");
		}else{
			page.setOrder(order);
		}
		if(null!=pageNo){
			page.setPageNo(Integer.parseInt(pageNo));
		}
				
		//查询所有权限，并放入会话
		page=this.menuManageService.findAllMenus(page,pfList);
		this.setRequestAttribute("menuList", page.getResult());

		//生成分页标签
		page.setForwordName(forwordName.toString());
		String tag=PageUtil.getTag(page);
		this.setRequestAttribute("tag", tag);
		//索引号
		this.setRequestAttribute("index", page.getFirst());
		
		//设置页面搜索初始值
		this.setRequestAttribute("menuName", menuName);
		this.setRequestAttribute("startDate", startDate);
		this.setRequestAttribute("endDate", endDate);
		
		String currentPage=request.getRequestURI().toString()+forwarCondition+page.getPageNo();
		this.setSessionAttribute("currentPage",currentPage );
		
		return SUCCESS;
	}
	
	/**
	 * 删除权限
	 */
	@Override
	public String delete() throws Exception {
		String id=this.getParameter("id");
		this.menuManageService.deleteById(Integer.parseInt(id));
		String currentPage=(String) this.getSessionAttribute("currentPage");
		this.response.sendRedirect(currentPage);		
		return null;

	}
	/**
	 * 查找权限
	 */
	public String toAlter() throws Exception {
		String id=this.getParameter("id");
		Menu menu=menuManageService.findById(Integer.parseInt(id));
		this.setRequestAttribute("menuInfo", menu);	

		//获取模块集合
		List<Menu>menuList=this.menuManageService.findAllMenus();
		this.setRequestAttribute("menuList",menuList);		
			
		return "alterMenu";
	}
	/**
	 * 修改权限
	 */
	@Override
	public String alter() throws Exception {
		//获取参数
		int id=Integer.parseInt(this.getParameter("id"));
		String menuName=this.getParameter("menuName");
		String url=this.getParameter("url");	
		String imageUrl=this.getParameter("imageUrl");	
		Integer parentId=Integer.parseInt(this.getParameter("parentId"));
		String orders=this.getParameter("orders");
		
		//修改菜单
		Menu menu=this.menuManageService.findById(id);
		
		menu.setMenuName(menuName);
		menu.setUrl(url);
		menu.setImageUrl(imageUrl);
		menu.setParentId(parentId);
		menu.setOrders(orders);
		
		this.menuManageService.alter(menu);
		String currentPage=(String) this.getSessionAttribute("currentPage");
		this.response.sendRedirect(currentPage);		
		return null;		
	}

	/**
	 * 去保存权限
	 */
	public String toSave() throws Exception {
		String checkMenuId=this.getParameter("id");
		
		List<Menu>menuList=this.menuManageService.findAllMenus();
		this.setRequestAttribute("menuList",menuList);		
		this.setRequestAttribute("checkMenuId",checkMenuId);	
		return "toSaveMenu";
}
	/**
	 * 保存菜单
	 */
	@Override
	public String save() throws Exception {
		//获取参数
		String menuName=this.getParameter("menuName");
		String url=this.getParameter("url");	
		String imageUrl=this.getParameter("imageUrl");	
		Integer parentId=Integer.parseInt(this.getParameter("parentId"));
		String orders=this.getParameter("orders");
		
		//保存菜单
		Menu menu=new Menu();
		
		menu.setMenuName(menuName);
		menu.setUrl(url);
		menu.setImageUrl(imageUrl);
		menu.setCreateTime(DateUtil.datetime());
		menu.setParentId(parentId);
		menu.setOrders(orders);
		
		this.menuManageService.save(menu);
		String currentPage=(String) this.getSessionAttribute("currentPage");
		this.response.sendRedirect(currentPage);		
		return null;
	}
	



}
