package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserManageService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("admin/")
public class TestController {
    @RequestMapping("testQueryId")
    public void testQueryUserId(){
        UserManageService userManageService = new UserManageService();
        User user = userManageService.getCurrentUser();
        System.out.println(user.getId());
    }
}
