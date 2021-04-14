package com.example.demo.controller;

import com.example.demo.dao.UserMapper;
import com.example.demo.model.User;
import com.example.demo.service.FileSaveService;
import com.example.demo.service.RegisterService;
import com.example.demo.service.UserManageService;
import com.example.demo.service.blockchain.MyBlockChainService;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;


@RestController
@RequestMapping("admin/")
@CrossOrigin(originPatterns = "*",allowCredentials="true",allowedHeaders = "*",methods = {})
public class ShiroController {
    @RequestMapping("charge")
    public String charge(String value){
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession(false);
        Object obj = session.getAttribute("userId");
        MyBlockChainService myBlockChainService = new MyBlockChainService((String) session.getAttribute("org"));

        assert obj==null;
        //System.out.println(obj+":"+value);
        try{
            myBlockChainService.chargeOrDraw(obj.toString(),Double.parseDouble(value));
        }catch(Exception e){
            e.printStackTrace();
            return "充值失败";
        }
        return "success";
    }

    @RequestMapping("draw")
    public String draw(String value){

        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession(false);
        Object obj = session.getAttribute("userId");
        MyBlockChainService myBlockChainService = new MyBlockChainService((String) session.getAttribute("org"));
        assert obj==null;
        //System.out.println(obj+":"+value);
        try{
            myBlockChainService.chargeOrDraw(obj.toString(),-1*Double.parseDouble(value));
        }catch(Exception e){
            e.printStackTrace();
            return "提现失败";
        }
        return "success";
    }
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
        Session session = subject.getSession(false);
        return (String) session.getAttribute("role");
    }

    @RequestMapping("register")
    public String register(@RequestBody User user) throws Exception {
        System.out.println("org ="+user.getOrgnization());
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
        Session session = subject.getSession(false);
        //System.out.println(subject.hasRole("user"));
        if(subject.hasRole("user"))
        {
            if(session!=null)
                return "hello with session userid "+session.getAttribute("userId");
            else
                return "hello with no session";
        }

        else
            return "need user role";
    }

    @RequestMapping("ahello")
    public String sayAdminHello(){
        Subject subject = SecurityUtils.getSubject();

        //System.out.println(subject.hasRole("admin"));
        //User user = (User) subject.getPrincipal();
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
