package com.common.tool.cache;

import java.util.Date;

import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;


/**
 * @className:OSCacheImp.java
 * @classDescription:
 * @author:xiayingjie
 * @createTime:Jun 27, 2008
 */

public class OSCacheManage {
	private static OSCacheManage myCacheManage;

	private static GeneralCacheAdministrator cache;

	/**
	 * 初始化缓存管理容器
	 */
	static void init() {
		try {
			if (cache == null)
				cache = new GeneralCacheAdministrator();
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	/**
	 * 获取CacheManager对象（饿汉单例模式）
	 * 
	 * @return
	 */
	public static OSCacheManage getInstance() {
		if (myCacheManage == null) {
			myCacheManage = new OSCacheManage();
			init();
		}
		return myCacheManage;

	}

	/**
	 * 设置Cache
	 * 
	 * @param key
	 * @param value
	 */
	public void setCache(String key, Object value) {
		cache.putInCache(key, value);
	}

	/**
	 * 获取cache
	 * 
	 * @param key
	 *            关键字
	 * @return
	 */
	public Object getCache(String key) {
		Object object = null;
		try {
			object = cache.getFromCache(key);
		} catch (NeedsRefreshException e) {

		}
		return object;
	}

	/**
	 * 根据key值清除缓存
	 * 
	 * @param key
	 *            关键字
	 */
	public void clearCache(String key) {
		cache.flushEntry(key);
	}

	/**
	 * 清除所有缓存
	 * 
	 * @param key
	 *            关键字
	 */
	public void clearAllCache() {
		cache.flushAll();
	}

	/**
	 * 根据日期清除缓存对象
	 */
	public void removeAll(Date date) {
		cache.flushAll(date);
	}
}
