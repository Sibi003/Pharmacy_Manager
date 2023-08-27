package com.interceptors;

import com.admin.Admin;
import com.admin.AdminService;
import com.dao.DBUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class AdminAuthorizer extends AbstractInterceptor{

	@Override
	public String intercept(ActionInvocation action) throws Exception {
		 System.out.println("admin Authorizer interceptor invoked");
		 Admin admin=(Admin)ActionContext.getContext().getSession().get("loggedAdmin");
		 if(admin==null)
		 {
			 System.out.println("adminn authorization failed");
			 
			/* int operationId=DBUtil.insertLog("admin",admin.getAdmin_id(),AdminService.getTodayDateAndTime(),"Admin's Session Expired");
			 if(operationId>0)
			 {
				 String details="Name: "+admin.getAdmin_name()+",email: "+admin.getAdmin_email();
				 if(DBUtil.insertLogDetails(operationId,details))
						 {
					  		System.out.println("admin seesion expiration or authorisation failure logged");
					  		
						 }else
						 {
							 System.out.println("admin authorisation failure or session expiration logging failed");
							 
						 }
			 }*/
			 
			 return "sessionExpired";
		 }
		 else {
			 System.out.println("admin authorisation success");
			 return  action.invoke();
		 }
		
	}
	

}
