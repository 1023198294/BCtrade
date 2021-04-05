package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.shiro.util.StringUtils;


public class User {
    private String id;
    private String username;
    private String password;
    private Boolean available;
    private String role;
    private String regtime;

    public User(String id, String username, String password, Boolean available, String role, String regtime) {
        //java.lang.String, java.lang.String, java.lang.String, java.lang.Boolean, java.lang.String, java.lang.String
        this.id = id;
        this.username = username;
        this.password = password;
        this.available = available;
        this.role = role;
        this.regtime = regtime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRegtime() {
        return regtime;
    }

    public void setRegtime(String regtime) {
        this.regtime = regtime;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", available=" + available +
                ", role='" + role + '\'' +
                ", regtime='" + regtime + '\'' +
                '}';
    }
}

