package com.interceptors;

import com.dao.*;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import com.user.*;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;

public class UserAuthenticator extends AbstractInterceptor {

	private static final long serialVersionUID = 8947547929191111377L;
	public String intercept(ActionInvocation invocation) throws Exception {
		System.out.print("interceptor invoked");
		HttpServletRequest request= ServletActionContext.getRequest();
		String username=request.getParameter("username");
		String password=request.getParameter("password");



		User user = DBUtil.getUserDB(username, password);

		if (user != null) {
			ActionContext.getContext().getSession().put("loggedUser", user);
			System.out.println("user fetched");
			return invocation.invoke();
		} else {
			System.out.println("invalid user");
			return "failure";
		}
	}



}





