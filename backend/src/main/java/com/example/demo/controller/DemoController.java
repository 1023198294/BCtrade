package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserManageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/hello")
public class DemoController {
    @GetMapping("/get")
    @ResponseBody
    public User getUserTemplate(){
        User user = new User("114514","野兽先辈","1919810",false,"admin","1919-8-10");
        return user;
    }



}
