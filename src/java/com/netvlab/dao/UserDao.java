package com.netvlab.dao;

import com.netvlab.model.Userinfo;

import java.util.List;

public interface UserDao {
    Userinfo getUser(Integer uid);
    List<Userinfo> getUserByStudentId(String stuid);
    String saveUser(Userinfo user);
    List<Userinfo> getAllUser();
}
