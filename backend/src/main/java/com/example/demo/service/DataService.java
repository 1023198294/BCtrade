package com.example.demo.service;

import com.example.demo.dao.DataMapper;
import com.example.demo.model.DataAsset;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class DataService {
    public DataAsset getDataIdByDataId(String dataId) throws IOException {
        String resource = "SqlMapConfig.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = factory.openSession();
        DataMapper dataMapper = session.getMapper(DataMapper.class);
        DataAsset dataAsset = dataMapper.getDataByDataId(dataId);
        return dataAsset;
    }
}
