package com.common.view.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.common.model.Action;
import com.common.model.Menu;
import com.common.model.Role;
import com.common.model.UserInfo;
import com.common.tool.cache.OSCacheManage;
import com.common.view.dto.RoleCache;

/**
 * @className:ManageFilter.java
 * @classDescription:
 * @author:zhangxiang
 * @createTime:May 17, 2010
 */
public class ManageFilter extends HttpServlet implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		// 获得登陆时的用户
		UserInfo userinfo = (UserInfo) req.getSession().getAttribute(
				"userAdmin");
		// 获取访问的url
		String requestUrl = req.getRequestURI();
		requestUrl = requestUrl.substring(requestUrl.indexOf("/", 2) + 1,
				requestUrl.length());

		// 申明缓存对象
		OSCacheManage osCacheManage = OSCacheManage.getInstance();
		if (requestUrl.indexOf("login") > -1) {
			filterChain.doFilter(request, response);
			return;
		}
		// 如果用户没有登录则跳到登录页面
		if (userinfo == null) {
			// 如果没有用户对象,则表示没有登陆。或是登陆的会话时间已过
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.sendRedirect(req.getContextPath()
					+ "/manage/login.jsp");
			return;
		}

		if (requestUrl.indexOf("manage") > -1) {
			Set<String> userUrlSet = (Set<String>) req.getSession()
					.getAttribute("userUrlSet");
			if (userUrlSet == null) {
				userUrlSet = new TreeSet();
				// 获取用户的所有权限，然后遍历权限，判断url是否有权访问
				for (Role role : userinfo.getRoles()) {
					// 如果缓存中没有，则将数据添加到缓存中
					if (null == osCacheManage.getCache("role_" + role.getId())) {
						RoleCache roleCache = new RoleCache();
						Set<String> urlSet = new TreeSet();
						for (Action action : role.getActions()) {
							urlSet.add(action.getPath());
						}
						Map menuMap = new HashMap();
						for (Menu menu : role.getMenus()) {
							if (!menuMap.containsKey(menu.getId())) {
								menuMap.put(menu.getId(), menu);
							}
						}
						// 设置菜单和url
						roleCache.setMenuMap(menuMap);
						roleCache.setUrlSet(urlSet);
						osCacheManage.setCache("role_" + role.getId(),
								roleCache);

					}
					RoleCache rc= (RoleCache) osCacheManage.getCache("role_" + role.getId());
					userUrlSet.addAll(rc.getUrlSet());

				}
			}
			for(String url:userUrlSet){
				if(requestUrl.equals(url)){
					filterChain.doFilter(request, response);
					return;
				}
			}

			// 如果没有权限则跳转到友好提示页面
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.sendRedirect(req.getContextPath()
					+ "/commons/noAction.jsp");
			return;
		} else {
			filterChain.doFilter(request, response);
			return;
		}
	}

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
