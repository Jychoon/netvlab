package com.netvlab.action;
import com.netvlab.model.Userinfo;
import com.opensymphony.xwork2.ActionSupport;
import com.netvlab.service.UserService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import javax.annotation.Resource;
@Controller("userAction")
@Scope("prototype")
public class UserAction extends ActionSupport {
    @Resource
    private UserService userService;

    public String m1(){
        Userinfo user=userService.getUser(3);
        System.out.println(user.getName());
        return SUCCESS;
    }
    public String saveUser(){
        Userinfo user=new Userinfo();
        user.setName("事务已提交");
        userService.saveUser(user);
        return SUCCESS;
    }
}