package com.example.demo;

import com.example.demo.model.User;
import com.example.demo.service.UserManageService;
import org.junit.jupiter.api.Test;

public class TestUserManager {
    @Test
    public void testQueryUserId(){
        UserManageService userManageService = new UserManageService();
        User user = userManageService.getCurrentUser();
        System.out.println(user.getId());
    }
}
