package com.example.demo;

import com.example.demo.dao.UserMapper;
import com.example.demo.model.User;
import com.example.demo.utils.DataUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;


public class TestRegister {
    @Test
    public void register() throws IOException {
        String id = DataUtils.generateShortUuid();
        Date date = new Date();
        String regTime = date.toString();
        User user = new User(id,"huangyangjun","114514",true,"user",regTime);
        String resource = "sqlMapConfig.xml";
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
    }
}
