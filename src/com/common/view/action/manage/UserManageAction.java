package com.common.view.action.manage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.common.model.Action;
import com.common.model.Menu;
import com.common.model.Role;
import com.common.model.UserInfo;
import com.common.tool.DateUtil;
import com.common.tool.Struts2Utils;
import com.common.tool.cache.OSCacheManage;
import com.common.tool.page.Condition;
import com.common.tool.page.Page;
import com.common.tool.page.PageUtil;
import com.common.tool.query.PropertyFilter;
import com.common.view.action.BaseAction;
import com.common.view.dto.RoleCache;




/**
 * @className:UserManageAction.java
 * @classDescription:用户管理Action
 * @author:xiayingjie
 * @createTime:2010-7-8
 */
@Namespace("/manage")
@Results({ 
	@Result(name = "input", location = "/manage/index/index.jsp"),
	@Result(name = "success", location = "/manage/user/userManage.jsp"),
	@Result(name = BaseAction.RELOAD, location = "/manage/user-manage.action", type = "redirect"),
	@Result(name = "login", location = "/manage/index/index.jsp"),
	@Result(name = "alterUser", location = "/manage/user/alterUser.jsp"), 
	@Result(name = "toSetRole", location = "/manage/user/setRole.jsp"), 	
	@Result(name = "error", location = "/manage/login.jsp") })
public class UserManageAction extends BaseAction {
	
	Page<UserInfo> page=new Page<UserInfo>(15);
	/**
	 * 登录
	 */
	public String login()throws Exception{
		String userName=this.getParameter("userName");
		String password=this.getParameter("password");
		UserInfo user=this.userManageService.login(userName, password);
		if(user!=null){
			this.setSessionAttribute("userAdmin", user);
			Map<Integer,Menu> userMap=new HashMap();
			for (Role role : user.getRoles()) {
				// 如果缓存中没有，则将数据添加到缓存中
				if (null == osCacheManage.getCache("role_"+role.getId())) {
					RoleCache roleCache=new RoleCache();
					Set<String> urlSet = new TreeSet();
					for (Action action : role.getActions()) {
						urlSet.add(action.getPath());
					}
					Map menuMap=new HashMap();
					for(Menu menu:role.getMenus()){
					    if(!menuMap.containsKey(menu.getId())){
					    	menuMap.put(menu.getId(), menu);
					    }
					}
					//设置菜单和url
					roleCache.setMenuMap(menuMap);
					roleCache.setUrlSet(urlSet);
					osCacheManage.setCache("role_"+role.getId(), roleCache);
				}
				RoleCache roleCache =(RoleCache) osCacheManage.getCache("role_"+role.getId());
		
				userMap.putAll(roleCache.getMenuMap());			
			}
			this.setSessionAttribute("userMenuMap", userMap);
			
			return LOGIN;
		}else{
			this.setRequestAttribute("message", "用户名或者密码错误");
			return ERROR;
		}	
	}
	
	/**
	 * 查找所有的人员
	 */
	public String list()throws Exception{
		//fileds
		String userName=this.getParameter("userName");
		String pageNo=this.getParameter("pageNo");
		String order=this.getParameter("order");
		String startDate=this.getParameter("startDate");
		String endDate=this.getParameter("endDate");
		
		StringBuffer condition=new StringBuffer();
		
		//查询条件
		PropertyFilter pf=new PropertyFilter("userName:LIKE_S",userName);
		PropertyFilter startPf=new PropertyFilter("createTime:GT_D",startDate);
		PropertyFilter endPf=new PropertyFilter("createTime:LT_D",endDate);
		List<PropertyFilter>pfList=new ArrayList();
		pfList.add(pf);
		pfList.add(startPf);
		pfList.add(endPf);
		
		//设置跳转页面
		StringBuffer forwordName=new StringBuffer(this.getRoot()+"/manage/user-manage.action");
	
		//获取分页跳转页面
		List<Condition> fragmentList=new ArrayList();
		fragmentList.add(new Condition("userName",userName,"匹配'"+userName+"'"));
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
				
		//查询所有用户，并放入会话
		page=this.userManageService.findAllUsers(page,pfList);
		this.setRequestAttribute("userList", page.getResult());
		//生成分页标签
		page.setForwordName(forwordName.toString());
		String tag=PageUtil.getTag(page);
		this.setRequestAttribute("tag", tag);
		//索引号
		this.setRequestAttribute("index", page.getFirst());
		
		//设置页面搜索初始值
		this.setRequestAttribute("userName", userName);
		this.setRequestAttribute("startDate", startDate);
		this.setRequestAttribute("endDate", endDate);
		
		String currentPage=request.getRequestURI().toString()+forwarCondition+page.getPageNo();
		this.setSessionAttribute("currentPage",currentPage );
	
		return SUCCESS;
	}
	
	/**
	 * 删除用户
	 */
	@Override
	public String delete() throws Exception {
		int id=Integer.parseInt(this.getParameter("id"));
		this.userManageService.deleteById(id);
		String currentPage=(String) this.getSessionAttribute("currentPage");
		this.response.sendRedirect(currentPage);		
		return null;

	}
	/**
	 * 去修改用户
	 */
	public String toAlter() throws Exception {
		int id=Integer.parseInt(this.getParameter("id"));
		UserInfo user=this.userManageService.findById(id);
		this.setRequestAttribute("userInfo", user);	

		return "alterUser";
	}
	/**
	 * 修改用户
	 */
	@Override
	public String alter() throws Exception {
		int id=Integer.parseInt(this.getParameter("id"));
		UserInfo user=this.userManageService.findById(id);
		
		String password=this.getParameter("password");
		String email=this.getParameter("email");
		String sex=this.getParameter("sex");
		String content=this.getParameter("content");
		String qq=this.getParameter("qq");

		user.setUserPassword(password);
		user.setEmail(email);
		user.setQq(qq);
		user.setContent(content);
		user.setSex(sex);
		
		this.userManageService.alter(user);
		
		String currentPage=(String) this.getSessionAttribute("currentPage");
		this.response.sendRedirect(currentPage);		
		return null;
	}
	/**
	 * 保存用户
	 */
	@Override
	public String save() throws Exception {
		//获取参数
		String userName=this.getParameter("userName");
		String password=this.getParameter("password");
		String email=this.getParameter("email");
		String sex=this.getParameter("sex");
		String content=this.getParameter("content");
		String qq=this.getParameter("qq");
		
		//保存用户
		UserInfo user=new UserInfo();

		user.setUserName(userName);
		user.setUserPassword(password);
		user.setEmail(email);
		user.setQq(qq);
		user.setContent(content);
		user.setSex(sex);
		
		user.setCreateTime(DateUtil.datetime());
		this.userManageService.save(user);
		return RELOAD;
	}
	
	/**
	 * 支持使用Jquery.validate Ajax检验用户名是否重复.
	 */
	public String checkUserName() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String userName = this.getParameter("userName");

		boolean flag=this.userManageService.checkUserName(userName);
		if (flag) {
			Struts2Utils.renderText("1");
		} else {
			Struts2Utils.renderText("0");
		}
		//因为直接输出内容而不经过jsp,因此返回null.
		return null;
	}
	/**
	 * 去为用户分配角色
	 * @return
	 */
	public String toSetRoleByUser(){
		//获取用户
		int userId=Integer.parseInt(this.getParameter("id"));
		UserInfo user=this.userManageService.findById(userId);
		//获取所有角色
		List<Role> roleList=this.roleManageService.findAllRoles();
		
		this.setRequestAttribute("user", user);
		this.setRequestAttribute("roleList", roleList);
		
		return "toSetRole";
	}
	/**
	 * 为用户分配角色
	 */
	public String setRoleByUser() throws Exception{
		//获取用户
		int userId=Integer.parseInt(this.getParameter("id"));
		UserInfo user=this.userManageService.findById(userId);
		//获取所有角色
		String[] rolesId=this.request.getParameterValues("roles");
		Set roleSet=new HashSet();
		for(String roleId:rolesId){
		    Role role=new Role();
		    role.setId(Integer.parseInt(roleId));
		    roleSet.add(role);
		}
		// 保存角色
		user.setRoles(roleSet);
		this.userManageService.alter(user);

		String currentPage=(String) this.getSessionAttribute("currentPage");
		this.response.sendRedirect(currentPage);		
		return null;
	}
	/**
	 * 登录
	 */
	public String loginOut()throws Exception{
		this.getSession().invalidate();
		return ERROR;
	}


}
