package com.netvlab.service.impl;

import com.netvlab.dao.UserDao;

import javax.annotation.Resource;

import com.netvlab.model.Userinfo;
import com.netvlab.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
@Service("userService")
public class UserServiceImpl implements UserService {
    //依赖Dao
    @Resource
    private UserDao userDao;
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public Userinfo getUser(Integer uid){
        return userDao.getUser(uid);
    }
    // 注入事务管理
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public void saveUser(Userinfo user){
        userDao.saveUser(user);
    }
}
