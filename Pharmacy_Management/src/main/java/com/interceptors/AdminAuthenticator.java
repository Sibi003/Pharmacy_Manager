package com.interceptors;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.admin.Admin;
import com.dao.DBUtil;

//import javax.servlet.http.HttpSession;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class AdminAuthenticator extends AbstractInterceptor{

	private static final long serialVersionUID = 1L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		
	 
	 System.out.println("admin interceptor invoked");
	 HttpServletRequest request= ServletActionContext.getRequest();
	 
	 String username=request.getParameter("username");
	 String password=request.getParameter("password");
	 Admin admin=DBUtil.getAdminDB(username, password);
	 
	 if(admin!=null)
	 {
		 
		 ActionContext.getContext().getSession().put("loggedAdmin", admin);
		 System.out.println("admin fetched");
		 
		 return invocation.invoke();
		 
	 }
	 else {
		 return "failure";
	 }
	 
	}
	
	

}
