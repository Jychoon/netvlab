package com.netvlab.action;

import com.netvlab.model.Userinfo;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller("logoutAction")
@Scope("prototype")
public class LogoutAction extends ActionSupport {
    public String logout(){
        Userinfo user = (Userinfo) ActionContext.getContext().getSession().get("user");
        ActionContext.getContext().getSession().clear();
        return SUCCESS;
    }
}
