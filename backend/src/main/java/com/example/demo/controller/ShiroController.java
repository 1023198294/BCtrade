package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.RegisterService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("admin/")
@CrossOrigin
public class ShiroController {
    @RequestMapping("login")
    public String login(String username, String password){
        Subject subject = SecurityUtils.getSubject();
        try{
            subject.login(new UsernamePasswordToken(username,password));
        }catch (UnknownAccountException e){
            return "账号不存在";
        }catch (AccountException e){
            return "账号密码不正确";
        }catch (IncorrectCredentialsException e){
            return "账号或密码不正确";
        }
        return "登陆成功";
    }

    @RequestMapping("register")
    public String register(@RequestBody User user) throws IOException {
        RegisterService registerService = new RegisterService();
        if(registerService.findExistUsernameOrNot(user.getUsername())){
            return "用户名已存在";
        }
        registerService.register(user);
        return "注册成功\n"+user.toString();
    }

    @CrossOrigin
    @RequestMapping("logout")
    public String logout() {
        //Subject subject = SecurityUtils.getSubject();
        //System.out.println("logout");
        //subject.logout();
        return "退出登录";
    }
    @RequestMapping("401")
    public String toLogin(){
        return "401页面，请先登录";
    }

    @RequestMapping("index")
    public String index() {
        return "index主页";
    }

    @RequestMapping("hello")
    public String sayHello(){
        Subject subject = SecurityUtils.getSubject();
        //System.out.println(subject.hasRole("user"));
        if(subject.hasRole("user"))
            return "hello";
        else
            return "need user role";
    }

    @RequestMapping("ahello")
    public String sayAdminHello(){
        Subject subject = SecurityUtils.getSubject();
        //System.out.println(subject.hasRole("admin"));
        if(subject.hasRole("admin"))
            return "hello";
        else
            return "need admin role";
    }

    @RequiresPermissions("admin:shiro:list")
    @RequestMapping("list")
    public String list() {
        return "list";
    }

    @RequiresPermissions("admin:shiro:add")
    @RequestMapping("add")
    public String add() {
        return "add";
    }

    @RequestMapping("unAuth")
    public String unAuth() {
        return "未认证";
    }

}
