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
 * @className:ModuleManageAction.java
 * @classDescription:模块管理Action
 * @author:xiayingjie
 * @createTime:2010-7-19
 */
@Namespace("/manage")
@Results({ 
	@Result(name = "success", location = "/manage/module/moduleManage.jsp"),
	@Result(name = BaseAction.RELOAD, location = "/manage/module-manage.action", type = "redirect"),
	@Result(name = "alterModule", location = "/manage/module/alterModule.jsp"), 
	@Result(name = "error", location = "/manage/login.jsp") })
public class ModuleManageAction extends BaseAction {
	
	Page<Module> page=new Page<Module>(15);
	
	/**
	 * 查找所有的模块
	 */
	public String list()throws Exception{
		//fileds
		String moduleName=this.getParameter("moduleName");
		String pageNo=this.getParameter("pageNo");
		String order=this.getParameter("order");
		String startDate=this.getParameter("startDate");
		String endDate=this.getParameter("endDate");
		
		StringBuffer condition=new StringBuffer();
		
		//查询条件
		PropertyFilter pf=new PropertyFilter("moduleName:LIKE_S",moduleName);
		PropertyFilter startPf=new PropertyFilter("createTime:GT_D",startDate);
		PropertyFilter endPf=new PropertyFilter("createTime:LT_D",endDate);
		List<PropertyFilter>pfList=new ArrayList();
		pfList.add(pf);
		pfList.add(startPf);
		pfList.add(endPf);
		
		//设置跳转页面
		StringBuffer forwordName=new StringBuffer(this.getRoot()+"/manage/module-manage.action");
	
		//获取分页跳转页面
		List<Condition> fragmentList=new ArrayList();
		fragmentList.add(new Condition("moduleName",moduleName,"匹配'"+moduleName+"'"));
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

		//初始化page属性值--默认id排序
		if(null==order){
			page.setOrder("id:asc");
		}else{
			page.setOrder(order);
		}
		if(null!=pageNo){
			page.setPageNo(Integer.parseInt(pageNo));
		}
				
		//查询所有权限，并放入会话
		page=this.moduleManageService.findAllModules(page,pfList);
		this.setRequestAttribute("moduleList", page.getResult());
		//生成分页标签
		page.setForwordName(forwordName.toString());
		String tag=PageUtil.getTag(page);
		this.setRequestAttribute("tag", tag);
		//索引号
		this.setRequestAttribute("index", page.getFirst());
		
		//设置页面搜索初始值
		this.setRequestAttribute("moduleName", moduleName);
		this.setRequestAttribute("startDate", startDate);
		this.setRequestAttribute("endDate", endDate);
		
		String currentPage=request.getRequestURI().toString()+forwarCondition+page.getPageNo();
		this.setSessionAttribute("currentPage",currentPage );
		
		return SUCCESS;
	}
	
	/**
	 * 删除角色
	 */
	@Override
	public String delete() throws Exception {
		int id=Integer.parseInt(this.getParameter("id"));
		this.moduleManageService.deleteById(id);
		String currentPage=(String) this.getSessionAttribute("currentPage");
		this.response.sendRedirect(currentPage);		
		return null;

	}
	/**
	 * 查找模块
	 */
	public String toAlter() throws Exception {
		int id=Integer.parseInt(this.getParameter("id"));
		Module module=moduleManageService.findById(id);
		this.setRequestAttribute("module", module);	
		
		
		return "alterModule";
	}
	/**
	 * 修改模块
	 */
	@Override
	public String alter() throws Exception {
		//获取参数
		int id=Integer.parseInt(this.getParameter("id"));
		String moduleName=this.getParameter("moduleName");
		String moduleInfo=this.getParameter("moduleInfo");

		
		//修改模块
		Module module=this.moduleManageService.findById(id);
  
		module.setModuleName(moduleName);
		module.setModuleInfo(moduleInfo);
		
		this.moduleManageService.alter(module);
		
		String currentPage=(String) this.getSessionAttribute("currentPage");
		this.response.sendRedirect(currentPage);		
		return null;
	}
	/**
	 * 保存模块
	 */
	@Override
	public String save() throws Exception {
		//获取参数
		String moduleName=this.getParameter("moduleName");
		String moduleInfo=this.getParameter("moduleInfo");

		
		//保存模块
		Module module=new Module();

		module.setModuleName(moduleName);
		module.setModuleInfo(moduleInfo);
		module.setCreateTime(DateUtil.datetime());
		
		this.moduleManageService.save(module);
		return RELOAD;
	}
	



}
