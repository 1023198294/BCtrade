package com.example.demo.service;

import com.example.demo.model.User;
import org.apache.shiro.SecurityUtils;

public class UserManageService {
    public User getCurrentUser(){
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        return user;
    }
}
