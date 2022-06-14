package com.netvlab.service;

import com.netvlab.model.Userinfo;

public interface LoginService {
    Userinfo  UserIdentification(Userinfo userinfo);

    String  UserRegister(Userinfo userionfo);
}
