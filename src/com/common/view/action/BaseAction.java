package com.common.view.action;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.common.service.IActionManageService;
import com.common.service.IMenuManageService;
import com.common.service.IModuleManageService;
import com.common.service.IRoleManageService;
import com.common.service.IUserManageService;
import com.common.tool.cache.OSCacheManage;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;


/**
 * @className:BaseAction.java
 * @classDescription:父类Action,包括一些通用的方法
 * @author:xiayingjie
 * @createTime:2010-6-24
 */
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
public abstract class BaseAction extends ActionSupport  implements  ServletRequestAware, ServletResponseAware{
	
	/** 进行增删改操作后,以redirect方式重新打开action默认页的result名.*/
	public static final String RELOAD = "reload";
	
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	// 申明缓存对象
	protected OSCacheManage osCacheManage = OSCacheManage.getInstance();
	
	boolean flag;
	//--  定义Service    --//
	@Autowired
	protected IUserManageService userManageService;
	@Autowired
	protected IRoleManageService roleManageService;
	@Autowired
	protected IActionManageService actionManageService;
	@Autowired
	protected IModuleManageService moduleManageService;
	@Autowired
	protected IMenuManageService menuManageService;

	
	//--  简化分页 --//
	
	
	
	
	/**
	 * Action函数, 默认的action函数, 默认调用list()函数.
	 */
	public String execute() throws Exception {
		return list();
	}

	//-- CRUD Action函数 --//
	/**
	 * Action函数,显示Entity列表界面.
	 * return SUCCESS.
	 */
	public abstract String list() throws Exception;

	/**
	 * Action函数,显示新增或修改Entity界面.
	 * return INPUT.
	 */
	public abstract String alter() throws Exception;

	/**
	 * Action函数,新增或修改Entity. 
	 * return RELOAD.
	 */
	public abstract String save() throws Exception;

	/**
	 * Action函数,删除Entity.
	 * return RELOAD.
	 */
	public abstract String delete() throws Exception;
	
	//--  简化取值----//
	/**
	 * 取得HttpRequest中Parameter的简化方法.
	 */
	public  String getParameter(String name) {
		return (null==this.request.getParameter(name))?null:this.request.getParameter(name);
	}
	/**
	 * 取得HttpRequest中Attribute的简化函数.
	 */
	public  Object getRequestAttribute(String name) {
		return request.getAttribute(name);
	}
	/**
	 * 取得HttpSession中Attribute的简化函数.
	 */
	public  Object getSessionAttribute(String name) {
		 return this.getSession().getAttribute(name);
	}
	/**
	 * 取得HttpSession的简化函数.
	 */
	public  HttpSession getSession() {
		return request.getSession();
	}


	/**
	 * 设置HttpRequest中Attribute的简化函数.
	 */
	public  boolean setRequestAttribute(String key,Object object) {
		try{
		     flag=false;
			 request.setAttribute(key,object);
			 flag=true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return flag;
		
	}
	/**
	 * 设置HttpSession中Attribute的简化函数.
	 */
	public  void setSessionAttribute(String name,Object object) {
			getSession().setAttribute(name,object);
	}
	/**
	 * 获取根目录
	 */
	public String getRoot(){
		return request.getContextPath();
	}
	/**
	 * 获取根目录
	 */
	public String getRealRoot(){
		return this.getSession().getServletContext().getRealPath("/");
	}

	//-------自动生成----------//
	public void setServletRequest(HttpServletRequest request) {
		this.request=request;
		
	}
	public void setServletResponse(HttpServletResponse response) {
		this.response=response;
		
	}

   //---------自动生成 service 的get set方法----------//
	/**
	 * @return the userManageService
	 */
	public IUserManageService getUserManageService() {
		return userManageService;
	}
	/**
	 * @param userManageService the userManageService to set
	 */
	public void setUserManageService(IUserManageService userManageService) {
		this.userManageService = userManageService;
	}
	/**
	 * @return the roleManageService
	 */
	public IRoleManageService getRoleManageService() {
		return roleManageService;
	}
	/**
	 * @param roleManageService the roleManageService to set
	 */
	public void setRoleManageService(IRoleManageService roleManageService) {
		this.roleManageService = roleManageService;
	}
	/**
	 * @return the actionManageService
	 */
	public IActionManageService getActionManageService() {
		return actionManageService;
	}
	/**
	 * @param actionManageService the actionManageService to set
	 */
	public void setActionManageService(IActionManageService actionManageService) {
		this.actionManageService = actionManageService;
	}
	/**
	 * @return the moduleManageService
	 */
	public IModuleManageService getModuleManageService() {
		return moduleManageService;
	}
	/**
	 * @param moduleManageService the moduleManageService to set
	 */
	public void setModuleManageService(IModuleManageService moduleManageService) {
		this.moduleManageService = moduleManageService;
	}
	/**
	 * @return the menuManageService
	 */
	public IMenuManageService getMenuManageService() {
		return menuManageService;
	}
	/**
	 * @param menuManageService the menuManageService to set
	 */
	public void setMenuManageService(IMenuManageService menuManageService) {
		this.menuManageService = menuManageService;
	}
	


}
