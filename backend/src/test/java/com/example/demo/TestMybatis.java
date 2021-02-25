package com.example.demo;

import com.example.demo.dao.UserMapper;
import com.example.demo.model.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

public class TestMybatis {
    @Test
    public void testInsertUser() throws IOException {
        String resource = "sqlMapConfig.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = factory.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        //User user = userMapper.findUserById(2);
        User user = new User("3","hyj","1234",true,"user","2001-01-10");
        userMapper.insertUser(user);
        //System.out.println(user);
        userMapper.deleteUserById("3");
        session.commit();
        session.close();
    }

    @Test
    public void findUserByNameTest() throws  IOException {
        String resource = "sqlMapConfig.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = factory.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        if(userMapper.findUserByName("hyj")!=null){
            System.out.println("exist");
        }else{
            System.out.println("not exist");
        }
    }
}
