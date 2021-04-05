package com.example.demo.dao;


import com.example.demo.model.User;

import java.util.List;

public interface UserMapper {
    public User findUserByName(String username);
    public User findUserById(String id);
    public List<User> findUserAll();
    public void insertUser(User user);
    public void deleteUserById(String id);
    public void updateUserPassword(User user);
    public void setInvalid(String id);
}