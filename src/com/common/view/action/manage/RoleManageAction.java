package com.common.view.action.manage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
 * @className:RoleManageAction.java
 * @classDescription:角色管理Action
 * @author:xiayingjie
 * @createTime:2010-7-16
 */
@Namespace("/manage")
@Results({ 
	@Result(name = "success", location = "/manage/role/roleManage.jsp"),
	@Result(name = BaseAction.RELOAD, location = "/manage/role-manage.action", type = "redirect"),
	@Result(name = "alterRole", location = "/manage/role/alterRole.jsp"),
	@Result(name = "toSetAction", location = "/manage/role/setAction.jsp"),
	@Result(name = "toSetMenu", location = "/manage/role/setMenu.jsp"),
	@Result(name = "error", location = "/manage/login.jsp",type = "redirect") })
public class RoleManageAction extends BaseAction {
	
	Page<Role> page=new Page<Role>(15);
	
	/**
	 * 查找所有的人员
	 */
	public String list()throws Exception{
		//fileds
		String roleName=this.getParameter("roleName");
		String pageNo=this.getParameter("pageNo");
		String order=this.getParameter("order");
		String startDate=this.getParameter("startDate");
		String endDate=this.getParameter("endDate");
		
		StringBuffer condition=new StringBuffer();
		
		//查询条件
		PropertyFilter pf=new PropertyFilter("roleName:LIKE_S",roleName);
		PropertyFilter startPf=new PropertyFilter("createTime:GT_D",startDate);
		PropertyFilter endPf=new PropertyFilter("createTime:LT_D",endDate);
		List<PropertyFilter>pfList=new ArrayList();
		pfList.add(pf);
		pfList.add(startPf);
		pfList.add(endPf);
		
		//设置跳转页面
		StringBuffer forwordName=new StringBuffer(this.getRoot()+"/manage/role-manage.action");
	
		//获取分页跳转页面
		List<Condition> fragmentList=new ArrayList();
		fragmentList.add(new Condition("roleName",roleName,"匹配'"+roleName+"'"));
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
		page=this.roleManageService.findAllRoles(page,pfList);
		this.setRequestAttribute("roleList", page.getResult());
		//生成分页标签
		page.setForwordName(forwordName.toString());
		String tag=PageUtil.getTag(page);
		this.setRequestAttribute("tag", tag);
		//索引号
		this.setRequestAttribute("index", page.getFirst());
		
		//设置页面搜索初始值
		this.setRequestAttribute("roleName", roleName);
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
		this.roleManageService.deleteById(id);
		String currentPage=(String) this.getSessionAttribute("currentPage");
		this.response.sendRedirect(currentPage);		
		return null;

	}
	/**
	 * 查找角色
	 */
	public String toAlter() throws Exception {
		int id=Integer.parseInt(this.getParameter("id"));
		Role role=roleManageService.findById(id);
		this.setRequestAttribute("role", role);	
		
		
		return "alterRole";
	}
	/**
	 * 修改用户
	 */
	@Override
	public String alter() throws Exception {
		//获取参数
		int id=Integer.parseInt(this.getParameter("id"));
		String roleName=this.getParameter("roleName");
		String roleInfo=this.getParameter("roleInfo");

		
		//修改角色
		Role role=this.roleManageService.findById(id);
  
		role.setRoleName(roleName);
		role.setRoleInfo(roleInfo);
		
		this.roleManageService.alter(role);
		
		String currentPage=(String) this.getSessionAttribute("currentPage");
		this.response.sendRedirect(currentPage);		
		return null;
	}
	/**
	 * 保存角色
	 */
	@Override
	public String save() throws Exception {
		//获取参数
		String roleName=this.getParameter("roleName");
		String roleInfo=this.getParameter("roleInfo");

		
		//保存角色
		Role role=new Role();

		role.setRoleName(roleName);
		role.setRoleInfo(roleInfo);
		role.setCreateTime(DateUtil.datetime());
		
		this.roleManageService.save(role);
		return RELOAD;
	}
	
	/**
	 * 去为角色分配权限
	 * @return
	 */
	public String toSetActionByRole(){
		//获取角色
		int roleId=Integer.parseInt(this.getParameter("id"));
		Role role=this.roleManageService.findById(roleId);
		//获取所有模块
		List<Module> moduleList=this.moduleManageService.findAllMoudles();
		
		this.setRequestAttribute("role", role);
		this.setRequestAttribute("moduleList", moduleList);
		
		return "toSetAction";
	}
	/**
	 * 为用户分配角色
	 */
	public String setActionByRole() throws Exception{
		//获取角色
		int roleId=Integer.parseInt(this.getParameter("id"));
		Role role=this.roleManageService.findById(roleId);
		//获取所有权限
		String[] actionsId=this.request.getParameterValues("actions");
		Set actionSet=new HashSet();
		for(String actionId:actionsId){
		    Action action=new Action();
		    action.setId(Integer.parseInt(actionId));
		    actionSet.add(action);
		}
		// 保存角色
		role.setActions(actionSet);
		this.roleManageService.alter(role);

		String currentPage=(String) this.getSessionAttribute("currentPage");
		this.response.sendRedirect(currentPage);		
		return null;
	}
	/**
	 * 去为角色分配菜单
	 * @return
	 */
	public String toSetMenuByRole(){
		//获取角色
		int roleId=Integer.parseInt(this.getParameter("id"));
		Role role=this.roleManageService.findById(roleId);
		//获取所有菜单
		List<Menu> menuList=this.menuManageService.findAllMenus();
		
		this.setRequestAttribute("role", role);
		this.setRequestAttribute("menuList", menuList);
		
		return "toSetMenu";
	}
	/**
	 * 为角色分配菜单
	 */
	public String setMenuByRole() throws Exception{
		//获取角色
		int roleId=Integer.parseInt(this.getParameter("id"));
		Role role=this.roleManageService.findById(roleId);
		//获取所有权限
		String[] menusId=this.request.getParameterValues("menus");
		Set<Menu> menuSet=new HashSet();
		for(String menuId:menusId){
			Menu menu=new Menu();
			menu.setId(Integer.parseInt(menuId));
		    menuSet.add(menu);
		}
		// 保存角色
		role.setMenus(menuSet);
		this.roleManageService.alter(role);

		String currentPage=(String) this.getSessionAttribute("currentPage");
		this.response.sendRedirect(currentPage);		
		return null;
	}
	/**
	 * 更新角色缓存(需重新登录)
	 * @return
	 * @throws Exception
	 */
	public String updateCacheByRole() throws Exception{
		//更新缓存
		String roleId=this.getParameter("id");
		this.osCacheManage.clearCache("role_"+roleId);
		this.getSession().invalidate();
		return ERROR;
	}

}
