package com.example.demo;

import com.example.demo.dao.DataMapper;
import com.example.demo.dao.UserMapper;
import com.example.demo.model.DataAsset;
import com.example.demo.utils.DataUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

public class DataInsertTest {
    @Test
    public void insertDataTest(){
        String resource = "SqlMapConfig.xml";
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = factory.openSession();
        DataMapper dataMapper = session.getMapper(DataMapper.class);
        String dataId = DataUtils.generateShortUuid();
        Date date = new Date();

        dataMapper.insertData(new DataAsset(
                dataId,"BDy0816H","BDy0816H",dataId,"114514.00",date.toString(),"1919810"
        ));
        //Date.parse(date.toString());
        session.commit();
        session.close();
    }
    @Test
    public void QueryIdTest(){
        String resource = "SqlMapConfig.xml";
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = factory.openSession();
        DataMapper dataMapper = session.getMapper(DataMapper.class);
        //String dataId = DataUtils.generateShortUuid();
        //Date date = new Date();

        //dataMapper.insertData(new DataAsset(
        //        dataId,"BDy0816H","BDy0816H",dataId,"114514.00",date,"1919810"
        //));
        List<String> lst = dataMapper.getDataIdById("BDy0816H");
        for(String s:lst)
            System.out.println(s);

        List<DataAsset> lst2 = dataMapper.getDataById("BDy0816H");
        for(DataAsset dataAsset:lst2){
            System.out.println(dataAsset.toString());
        }
        session.commit();
        session.close();
    }
}

