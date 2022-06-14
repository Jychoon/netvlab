package com.netvlab.service;

import com.netvlab.model.Userinfo;

public interface UserService {
    Userinfo getUser(Integer uid);
    void saveUser(Userinfo user);
}
