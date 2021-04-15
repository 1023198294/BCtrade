package com.example.demo;

import com.example.demo.dao.DataInfoMapper;
import com.example.demo.dao.TradeInfoMapper;
import com.example.demo.dao.UserMapper;
import com.example.demo.model.DataInfo;
import com.example.demo.model.TradeInfo;
import com.example.demo.model.User;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

public class TestMybatis {
    @Test
    public void testInsertUser() throws IOException {
        String resource = "SqlMapConfig.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = factory.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        //User user = userMapper.findUserById(2);
        User user = new User("3","hyj","1234",true,"user","2001-01-10","2");
        userMapper.insertUser(user);
        //System.out.println(user);
        userMapper.deleteUserById("3");
        session.commit();
        session.close();
    }

    @Test
    public void findUserByNameTest() throws  IOException {
        String resource = "SqlMapConfig.xml";
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

    @Test
    public void testSelectByLike() throws  IOException {
        String resource = "SqlMapConfig.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = factory.openSession();
        //UserMapper userMapper = session.getMapper(UserMapper.class);
        DataInfoMapper dataInfoMapper = session.getMapper(DataInfoMapper.class);

        PageHelper.startPage(0,2,"dataname ASC");
        List<DataInfo> res = dataInfoMapper.getDataInfoByLikeName("test");
        for(DataInfo d:res){
            System.out.println(d.toString());
        }
        PageHelper.startPage(0,2);
        long total = PageHelper.count(new ISelect() {
            @Override
            public void doSelect() {
                dataInfoMapper.getDataInfoByLikeName("test");
            }
        });
        System.out.println("total pages="+total);
        res = dataInfoMapper.getDataInfoByLikeName("test");
        for(DataInfo d:res){
            System.out.println(d.toString());
        }
    }
    @Test
    public void testTradeInfo() throws IOException {
        String resource = "SqlMapConfig.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = factory.openSession();
        TradeInfoMapper tradeInfoMapper = session.getMapper(TradeInfoMapper.class);
        //User user = userMapper.findUserById(2);
        TradeInfo tradeInfo1 = new TradeInfo(0,"dtid0","usrid0","usrid1","usrid0",1.0,"32");
        TradeInfo tradeInfo2 = new TradeInfo(0,"dtid1","usrid1","usrid2","usrid0",0.3,"32");
        TradeInfo tradeInfo3 = new TradeInfo(0,"dtid2","usrid2","usrid3","usrid0",0.1,"32");

        //tradeInfoMapper.insertTradeInfo(tradeInfo1);
        //tradeInfoMapper.insertTradeInfo(tradeInfo2);
        //tradeInfoMapper.insertTradeInfo(tradeInfo3);
        //System.out.println(user);
        List<TradeInfo> tradeInfosByid1 = tradeInfoMapper.getTradeInfoByUserId("usrid1");
        List<TradeInfo> tradeInfosAll = tradeInfoMapper.getAllTradeInfo();
        System.out.println(tradeInfosByid1.toString());
        System.out.println(tradeInfosAll.toString());
        session.commit();
        session.close();
    }
}
