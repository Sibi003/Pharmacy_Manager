package com.interceptors;


import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.user.User;
public class UserAuthorizer extends AbstractInterceptor{

    @Override
    public String intercept(ActionInvocation actionInvocation) throws Exception {
        System.out.println("authorizer interceptor invoked");
        User user=(User)ActionContext.getContext().getSession().get("loggedUser");
        if(user==null)
        {
            System.out.println("user authorization failed");
            return "sessionExpired";
        }else {
            return actionInvocation.invoke();

        }
    }
}
