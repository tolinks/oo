package com.common.tool.page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @className:PageUtil.java
 * @classDescription:实现分页接口
 * @author:xiayingjie
 * @createTime:2010-7-12
 */
public class PageUtil{

	/**
	 * 获取分页显示标签
	 * 
	 * @param page
	 *            page对象
	 * @return
	 */
	public static String getTag(Page page) {
		StringBuffer tag=new StringBuffer();
		
		tag.append("  <a href='" + page.getForwordName()
				+ 1 + "'>首页</a>&nbsp;");
		tag.append("  <a href='" + page.getForwordName()
				+ page.getPrePage() + "'>上页</a>&nbsp;");
		tag.append("  <a href='" + page.getForwordName()
				+ page.getNextPage() + "'>下页</a>&nbsp;");
		tag.append("  <a href='" + page.getForwordName()
				+ page.getTotalPages() + "'>尾页</a>&nbsp;");
		

		tag.append("<br/>共" + page.getTotalCount() + "条  每页" + page.getPageSize() + "条  总共"
				+ page.getTotalPages() + "页 " + page.getPageNo() + "/" + page.getTotalPages() + "  ");
		
		return tag.toString();

	}
	/**
	 * 获取跳转的条件
	 * @param map
	 * @return
	 */
	public static  String getForwardCondition(List<Condition> list){
		StringBuffer fragment=new StringBuffer(getCondition(list,null));

		if("".equals(fragment.toString())){
			fragment.append("?pageNo=");
		}else{
			fragment.append("&pageNo=");
		}
		return fragment.toString();
	}
	/**
	 * 获取排序URL
	 * @param map
	 * @return
	 */
	public static  String getOrderCondition(List<Condition> list){
		
		StringBuffer fragment=new StringBuffer(getCondition(list,"order"));

		if("".equals(fragment.toString())){
			fragment.append("?order=");
		}else{
			fragment.append("&order=");
		}
		return fragment.toString();
	}
	/**
	 * 获取最基本的条件URL
	 * @param list
	 * @return
	 */
	public static String getCondition(List<Condition> list,String removeName){
		boolean flag=true;
		StringBuffer fragment=new StringBuffer("");
		for(Condition condition:list){
			if(!condition.getName().equals(removeName)){
				if(null!=condition.getValue()&&!"".equals(condition.getValue())){
					if(flag){
						fragment.append("?");
						flag=false;
					}else{
						fragment.append("&");
					}
					fragment.append(condition.getName()+"="+condition.getValue());
				}
			}
		}
		return fragment.toString();
	}
	/**
	 * 获取过滤条件查询
	 * @param list
	 * @return
	 */
	public static List<Condition> getFilterConditions(List<Condition> list){
		List<Condition> fragmentList=new ArrayList();
		for(Condition condition:list){
			if(!"".equals(condition.getValue())&&null!=condition.getValue()){
				//暂时屏蔽排序显示
				if(condition.isFlag()){
					condition.setUrl(getCondition(list,condition.getName()));
					fragmentList.add(condition);
				}
			}
		}
		return fragmentList;
		
	}
//
//	/**
//	 * 获取分页显示标签
//	 * 
//	 * @param page
//	 *            page对象
//	 * @return
//	 */
//	public static String[] getWMLTag(Page page) {
//		// 总页数
//		int allCount = page.getAllCount();
//		// 显示的条数
//		int count = page.getCount();
//		// 当前的页数
//		int currentPage = page.getPage();
//		// 左边显示的条数
//		int leftCount = page.getLeftCount();
//		// 右边显示的条数
//		int rightCount = page.getRigthCount();
//		// 如果显示为0的话则默认为三条
//		if (leftCount == 0 || rightCount == 0) {
//			leftCount = 3;
//			rightCount = 3;
//		}
//		// 总页数
//		int allPage;
//		if (allCount % count == 0) {
//			allPage = allCount / count;
//		} else {
//			allPage = allCount / count + 1;
//		}
//
//		String arg1 = "";
//		// 如果当前页小于首页，将当前页设为首页
//		StringBuffer tag = new StringBuffer();
//		if (currentPage < 1) {
//			currentPage = 1;
//		}
//		// 如果中当前页大约末页，则将当前页设置为末页
//		if (currentPage > allPage) {
//			currentPage = allPage;
//		}
//		if (currentPage < allPage) {
//			tag.append("  <a href='" + page.getForwordName()
//					+ (currentPage + 1) + "'>下页</a>&nbsp;");
//		} 
//		if (currentPage > 1) {
//			tag.append("<a href='" + page.getForwordName() + (currentPage - 1)
//					+ "'>上页</a>&nbsp;<br/>");
//		}
//
//		// 显示条数
//		String[] args = new String[5];
//		args[0] = "共找到" + allCount + "条结果  ";
//		args[1] = tag.toString();
//		args[2]="第" + currentPage + "页 " + "共"+allPage+"页";
//		args[3]=String.valueOf((currentPage-1)*count);
//		
//		return args;
//	}
//
//	public static String getTagByChoose(Page page) {
//		// 总页数
//		int allCount = page.getAllCount();
//		// 显示的条数
//		int count = page.getCount();
//		// 当前的页数
//		int currentPage = page.getPage();
//		// 左边显示的条数
//		int leftCount = page.getLeftCount();
//		// 右边显示的条数
//		int rightCount = page.getRigthCount();
//		// 如果显示为0的话则默认为三条
//		if (leftCount == 0 || rightCount == 0) {
//			leftCount = 3;
//			rightCount = 3;
//		}
//
//		// 总页数
//		int allPage;
//		if (allCount % count == 0) {
//			allPage = allCount / count;
//		} else {
//			allPage = allCount / count + 1;
//		}
//		// 如果当前页小于首页，将当前页设为首页
//		StringBuffer tag = new StringBuffer();
//		if (currentPage < 1) {
//			currentPage = 1;
//		}
//		// 如果中当前页大约末页，则将当前页设置为末页
//		if (currentPage > allPage) {
//			currentPage = allPage;
//		}
//		tag.append("共" + allCount + "条 每页" + page.getCount() + "条  总共"
//				+ allPage + "页 " + currentPage + "/" + allPage + "  ");
//		if (allPage > 1) {
//			tag.append("<a href='" + page.getForwordName() + 1
//					+ "'>首页</a>&nbsp;");
//			tag.append("<a href='" + page.getForwordName() + (currentPage - 1)
//					+ "'>上页</a>&nbsp;");
//
//			if (currentPage != allPage) {
//				tag.append("  <a href='" + page.getForwordName()
//						+ (currentPage + 1) + "'>下页</a>&nbsp;");
//			} else {
//				tag.append("<a href='" + page.getForwordName() + currentPage
//						+ "'>下页</a>&nbsp;");
//			}
//
//			tag.append("<a href='" + page.getForwordName() + allPage
//					+ "'>末页</a>&nbsp;");
//			// tag.append("<input name=\"pageCountName\" type=\"text\"
//			// size=\"5\" />");
//			// tag.append("<a href=\"javascript:var
//			// count=document.getElementById('pageCountName').value;location.href='"+page.getForwordName()+"'+count+'&flag=1';\">跳转</a>");
//
//			// tag.append("<form name=\"dispatchForm\" method=\"post\" action=\"
//			// \">");
//			// tag.append("<input name=\"pageCountName\" type=\"text\"
//			// size=\"5\" />");
//			// tag.append("<a href=\"javascript:var
//			// count=document.getElementById('pageCountName').value;document.dispatchForm.action='"+page.getForwordName()+"'+count;document.dispatchForm.submit();\">跳转</a>");
//			// tag.append("</form>");
//			// tag.append("<a
//			// href=\"javascript:MM_jumpMenu('"+page.getForwordName()+"');\">跳转</a>");
//			// tag.append("<script language=\"javascript\">");
//			// tag.append(" function MM_jumpMenu(count){ ");
//			// tag.append(" var
//			// i=document.getElementById('pageCountName').value; var
//			// url=count+i;");
//			// tag.append(" window.location.href=url;");
//			// tag.append(" }");
//			// tag.append("</script>");
//		}
//		return tag.toString();
//	}
}
