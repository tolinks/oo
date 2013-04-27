package com.common.view.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.common.model.Menu;

/**
 * @className:RoleCache.java
 * @classDescription:缓存对象
 * @author:xiayingjie
 * @createTime:2010-7-23
 */
public class RoleCache {
	private  Set<String> urlSet=new TreeSet();
	private  Map<Integer,Menu>  menuMap=new HashMap();
	/**
	 * @return the urlSet
	 */
	public Set<String> getUrlSet() {
		return urlSet;
	}
	/**
	 * @param urlSet the urlSet to set
	 */
	public void setUrlSet(Set<String> urlSet) {
		this.urlSet = urlSet;
	}
	/**
	 * @return the menuMap
	 */
	public Map<Integer, Menu> getMenuMap() {
		return menuMap;
	}
	/**
	 * @param menuMap the menuMap to set
	 */
	public void setMenuMap(Map<Integer, Menu> menuMap) {
		this.menuMap = menuMap;
	}





}
