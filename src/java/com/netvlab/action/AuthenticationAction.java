package com.netvlab.action;

import com.netvlab.dao.ExperimentstatisticsDao;
import com.netvlab.dao.TabExperimentRecordDao;
import com.netvlab.dao.UserDao;
import com.netvlab.model.Userinfo;
import com.netvlab.model.experimentmanage.TabExperimentrecord;
import com.netvlab.service.LoginService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.List;

@Controller("authenticationAction")
@Scope("prototype")
public class AuthenticationAction extends ActionSupport implements ModelDriven<Userinfo> {
    //model driven
    private Userinfo user;
    @Override
    public Userinfo getModel() {
        if(user == null){
            user = new Userinfo();
        }
        return user;
    }

    @Resource
    private LoginService loginService;
    @Resource
    private TabExperimentRecordDao tabExperimentRecordDao;
    @Resource
    private ExperimentstatisticsDao experimentstatisticsDao;
    @Resource
    private UserDao userDao;

    public String authentication(){
        /*//生成实验记录
        List<TabExperimentrecord> tabExperimentrecords = tabExperimentRecordDao.getAll();
        for(TabExperimentrecord tabExperimentrecord : tabExperimentrecords){
            experimentstatisticsDao.updateExp(tabExperimentrecord);
        }*/
        System.out.println("登陆中.........");

        Userinfo userinfo = loginService.UserIdentification(user);
        if(userinfo == null){
            return ERROR;
        }
        if(ActionContext.getContext().getSession().get("user")!=null){
            ActionContext.getContext().getSession().clear();
        }
        ActionContext.getContext().getSession().put("user",userinfo);
        System.out.println(userinfo.getName());
        return SUCCESS;
    }
}