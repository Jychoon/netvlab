package com.netvlab.service.impl;

import com.netvlab.dao.UserDao;
import com.netvlab.model.Userinfo;
import com.netvlab.service.LoginService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
@Service("loginService")
public class LoginServiceImpl implements LoginService {

    @Resource
    private UserDao userDao;

    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public Userinfo UserIdentification(Userinfo userinfo) {
        if(userinfo == null)    return  null;
        if(userinfo.getStudentId() == null)     return  null;
        List<Userinfo> res = userDao.getUserByStudentId(userinfo.getStudentId());
        if(res == null){
            //查无此人
        }else if(res.size() == 1){
            //存在匹配StudentId
            if(res.get(0).getPassword().equals(userinfo.getPassword())){
                return res.get(0);
            }
        }else if(res.size() >1 ){
            //存在多个匹配StudentId（理论上不会出现这种情况）
            for(Userinfo temp : res){
                if(temp.getPassword().equals(userinfo.getPassword()))   return temp;
            }
        }
        return null;
    }

    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public String UserRegister(Userinfo userinfo) {
        List<Userinfo> res = userDao.getUserByStudentId(userinfo.getStudentId());
        if(res.size()>0)   return "userExist";
        String newStudentId=userDao.saveUser(userinfo);
        if(newStudentId==null)  return "registerFailed";
        return "registerSuccess";
    }


}
