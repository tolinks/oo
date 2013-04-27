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
 * @className:ActionManageAction.java
 * @classDescription:角色管理Action
 * @author:xiayingjie
 * @createTime:2010-7-16
 */
@Namespace("/manage")
@Results({ 
	@Result(name = "success", location = "/manage/action/actionManage.jsp"),
	@Result(name = BaseAction.RELOAD, location = "/manage/action-manage.action", type = "redirect"),
	@Result(name = "alterAction", location = "/manage/action/alterAction.jsp"), 
	@Result(name = "toSaveAction", location = "/manage/action/addAction.jsp"), 
	@Result(name = "error", location = "/manage/login.jsp") })
public class ActionManageAction extends BaseAction {
	
	Page<Action> page=new Page<Action>(15);
	
	/**
	 * 查找所有的人员
	 */
	public String list()throws Exception{
		//fileds
		String actionName=this.getParameter("actionName");
		String pageNo=this.getParameter("pageNo");
		String order=this.getParameter("order");
		String startDate=this.getParameter("startDate");
		String endDate=this.getParameter("endDate");
		String moduleId=this.getParameter("moduleId");
		
		StringBuffer condition=new StringBuffer();
		
		//查询条件
		PropertyFilter pf=new PropertyFilter("actionName:LIKE_S",actionName);
		PropertyFilter modulePf=new PropertyFilter("moduleId:EQ_I",moduleId);
		PropertyFilter startPf=new PropertyFilter("createTime:GT_D",startDate);
		PropertyFilter endPf=new PropertyFilter("createTime:LT_D",endDate);
		List<PropertyFilter>pfList=new ArrayList();
		pfList.add(pf);
		pfList.add(modulePf);
		pfList.add(startPf);
		pfList.add(endPf);
		
		//设置跳转页面
		StringBuffer forwordName=new StringBuffer(this.getRoot()+"/manage/action-manage.action");
	
		String moduleName=null;
		if(null!=moduleId && !"".equals(moduleId)){
			moduleName=this.moduleManageService.findById(Integer.parseInt(moduleId)).getModuleName();
		}
		
		//获取分页跳转页面
		List<Condition> fragmentList=new ArrayList();
		fragmentList.add(new Condition("actionName",actionName,"匹配'"+actionName+"'"));
		fragmentList.add(new Condition("moduleId",moduleId,"'"+moduleName+"'"));
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
		page=this.actionManageService.findAllActions(page,pfList);
		this.setRequestAttribute("actionList", page.getResult());
		List<Module>moduleList=this.moduleManageService.findAllMoudles();
		this.setRequestAttribute("moduleList", moduleList);
		//生成分页标签
		page.setForwordName(forwordName.toString());
		String tag=PageUtil.getTag(page);
		this.setRequestAttribute("tag", tag);
		//索引号
		this.setRequestAttribute("index", page.getFirst());
		
		//设置页面搜索初始值
		this.setRequestAttribute("moduleId", moduleId);
		this.setRequestAttribute("actionName", actionName);
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
		this.actionManageService.deleteById(Integer.parseInt(id));
		String currentPage=(String) this.getSessionAttribute("currentPage");
		this.response.sendRedirect(currentPage);		
		return null;

	}
	/**
	 * 查找权限
	 */
	public String toAlter() throws Exception {
		String id=this.getParameter("id");
		Action action=actionManageService.findById(Integer.parseInt(id));
		this.setRequestAttribute("action", action);	

		//获取模块集合
		List<Module> moduleList=this.moduleManageService.findAllMoudles();
		this.setRequestAttribute("moduleList",moduleList);	
			
		return "alterAction";
	}
	/**
	 * 修改权限
	 */
	@Override
	public String alter() throws Exception {
		//获取参数
		String id=this.getParameter("id");
		String actionName=this.getParameter("actionName");
		String path=this.getParameter("path");
		int orders=Integer.parseInt(this.getParameter("orders"));
		Integer moduleId=Integer.parseInt(this.getParameter("moduleId"));
	

		
		//修改权限
		Action action=this.actionManageService.findById(Integer.parseInt(id));
		
  
		action.setActionName(actionName);
		action.setPath(path);
		Module module=new Module();
		module.setId(moduleId);
		action.setModule(module);
		action.setOrders(orders);

		
		this.actionManageService.alter(action);
		
		String currentPage=(String) this.getSessionAttribute("currentPage");
		this.response.sendRedirect(currentPage);		
		return null;
	}

	/**
	 * 查找权限
	 */
	public String toSave() throws Exception {
		List<Module> moduleList=this.moduleManageService.findAllMoudles();
		this.setRequestAttribute("moduleList",moduleList);	
			
		return "toSaveAction";
}
	/**
	 * 保存权限
	 */
	@Override
	public String save() throws Exception {
		//获取参数
		String actionName=this.getParameter("actionName");
		String path=this.getParameter("path");		
		Integer moduleId=Integer.parseInt(this.getParameter("moduleId"));
		int orders=Integer.parseInt(this.getParameter("orders"));
		
		//保存权限
		Action action=new Action();
		
		action.setActionName(actionName);
		action.setPath(path);
		Module module=new Module();
		module.setId(moduleId);
		action.setModule(module);
		action.setCreateTime(DateUtil.datetime());
		action.setOrders(orders);
		
		this.actionManageService.save(action);
		String currentPage=(String) this.getSessionAttribute("currentPage");
		this.response.sendRedirect(currentPage);		
		return null;
	}
	



}
