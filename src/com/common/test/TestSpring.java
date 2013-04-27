package com.common.test;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.common.dao.ActionDAO;
import com.common.dao.DAOInterface;
import com.common.dao.UserInfoDAO;
import com.common.model.Action;
import com.common.model.Module;
import com.common.model.Role;
import com.common.model.UserInfo;
import com.common.service.IUserManageService;
import com.common.service.imp.UserManageService;



/**
 * <p>
 * Title: an Test.java file of the portal project.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Create Time: 2009-11-5 下午02:35:13
 * </p>
 * <p>
 * Company: Guangzhou Zhengshi Network Tech Co., Ltd
 * </p>
 * 
 * @author Zhantao Feng
 * @version 1.0
 */
@SuppressWarnings("unchecked")
public class TestSpring {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("config/spring/applicationContext.xml"
				);
		BeanFactory factory = (BeanFactory) appContext;
//		DAOInterface<UserInfo> userInfoDAO = (UserInfoDAO) factory.getBean("userInfoDAO");
		DAOInterface<Action> actionDAO = (DAOInterface<Action>) factory.getBean("actionDAO");
		DAOInterface<Role> roleDAO = (DAOInterface<Role>) factory.getBean("roleDAO");
		DAOInterface<Module> moduleDAO = (DAOInterface<Module>) factory.getBean("moduleDAO");
		String hql="from Role where id=22";
		String hql2="from Action";
		Role role=roleDAO.find(hql);
//		List<Action> actions=actionDAO.findList(hql2);
//		Set actionSet=new HashSet();
//		for(Action ac:actions){
//			actionSet.add(ac);
//		}
		Set<Action> list=role.getActions();
		for(Action ac:list){
			ac.getActionName();
		}
//		role.setActions(actionSet);
//		roleDAO.alter(role);
//		IUserManageService  um=(IUserManageService) factory.getBean("userManageService");
//		um.login("aa", "bb");
//		UserInfo user=um.findById(2);
//		moduleDAO.deleteById(2);
//		roleDAO.deleteById(21);
//		String hql="from Role";
//		roleDAO.findCountBySql(hql);
//		System.out.println(role.getRoleName());
//		String hql="from Action";
//		roleDAO.deleteById(2);
//	List<UserInfo> a=userInfoDAO.findList(hql);
//		String sql="from action";
//		UserInfo  user=userInfoDAO.findById(2);
//		userInfoDAO.deleteById(4);
//		System.out.println(user.getUserName()+"====");
		//System.out.println(c);
		
//		for(UserInfo u:a){
//			Set<Role> r= u.getRoles();
//			for(Role rs:r){
//				System.out.println(rs.getRoleName());
//			}
//			//System.out.println(u.getUserName());
//		}
		
		
	}

}
