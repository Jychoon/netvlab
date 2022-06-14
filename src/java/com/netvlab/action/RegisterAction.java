package com.netvlab.action;

import com.netvlab.model.Userinfo;
import com.netvlab.service.LoginService;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

@Controller("registerAction")
@Scope("prototype")
public class RegisterAction extends ActionSupport {

    @Autowired(required = false)
    private String studentId;
    @Autowired(required = false)
    private String name;
    @Autowired(required = false)
    private String school;
    @Autowired(required = false)
    private String classes;
    @Autowired(required = false)
    private String password;

    @Autowired(required = false)
    private JSONObject returnJsonData = new JSONObject();

    @Resource
    private LoginService loginService;

    public String register(){
        System.out.println("register Action..............");

        Userinfo userinfo=new Userinfo();
        userinfo.setStudentId(studentId);
        userinfo.setName(name);
        userinfo.setSchool(school);
        userinfo.setClasses(classes);
        userinfo.setPassword(password);

        String res=loginService.UserRegister(userinfo);
        returnJsonData.put("result",res);

        System.out.println("register over..............");
        return SUCCESS;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public JSONObject getReturnJsonData() {
        return returnJsonData;
    }

    public void setReturnJsonData(JSONObject returnJsonData) {
        this.returnJsonData = returnJsonData;
    }
}
