package com.example.demo.service;

import com.example.demo.dao.UserMapper;
import com.example.demo.model.User;
import com.example.demo.service.blockchain.MyBlockChainService;
import com.example.demo.utils.DataUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class RegisterService {
    public void register(User user) throws Exception {
        String id = DataUtils.generateShortUuid();
        Date date = new Date();
        String regTime = date.toString();
        //User user = new User(id,userIn.getUsername(),userIn.getPassword(),userIn.getAvailable(),userIn.getRole(),regTime);
        user.setId(id);
        user.setRegtime(regTime);
        System.out.println(user.getUsername());
        String resource = "SqlMapConfig.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = factory.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        //User user = userMapper.findUserById(2);
        userMapper.insertUser(user);
        //System.out.println(user);
        //userMapper.deleteUserById("3");
        session.commit();
        session.close();
        ///*
        MyBlockChainService myBlockChainService = new MyBlockChainService(user.getOrgnization());
        myBlockChainService.registerUser(id);
        myBlockChainService.createWallet(id,0.0);
        //*/
    }

    public User findUserByName(String username) throws IOException{
        String resource = "SqlMapConfig.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = factory.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        return userMapper.findUserByName(username);
    }
    public boolean findExistUsernameOrNot(String username) throws IOException{
        String resource = "SqlMapConfig.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = factory.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        return userMapper.findUserByName(username) != null;
    }
}
