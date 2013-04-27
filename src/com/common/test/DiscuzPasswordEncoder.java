package com.common.test;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @className:DiscuzPasswordEncode.java
 * @classDescription:
 * @author:xiayingjie
 * @param <implement>
 * @createTime:2010-8-12
 */
public class DiscuzPasswordEncoder implements  PasswordEncoder{
	 private static final String QUERY_USER_SQL = "select * from user_info where username = ?";  
	public String encode(String password) {
		// TODO Auto-generated method stub
		
	//	JdbcTemplate template = new JdbcTemplate(); 
	//	template.execute("");
		return MD5.getInstance().createMD5(MD5.getInstance().createMD5(password)+"asdf");
	}

	public static void main(String[] arg){
		DiscuzPasswordEncoder de=new DiscuzPasswordEncoder();
		System.out.println(de.encode("asdf"));
	}
}
