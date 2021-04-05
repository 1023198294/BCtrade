package com.example.demo.service;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.DataInfoMapper;
import com.example.demo.dao.DataMapper;
import com.example.demo.dao.ReportMapper;
import com.example.demo.dao.UserMapper;
import com.example.demo.model.Report;
import com.example.demo.utils.DataJSON;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ReportService {
    public void insertReport(Report report) throws IOException {
        String resource = "SqlMapConfig.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = factory.openSession(false);
        ReportMapper reportMapper = session.getMapper(ReportMapper.class);
        reportMapper.insertReport(report);
        session.commit();
        session.close();
    }
    public JSONObject getAllReport(int page, int pageSize) throws IOException {
        String resource = "SqlMapConfig.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = factory.openSession(false);

        ReportMapper reportMapper = session.getMapper(ReportMapper.class);
        List<Report> reports;
        PageHelper.startPage(page,pageSize);
        reports = reportMapper.getAllReport();
        long total = PageHelper.count(new ISelect() {
                @Override
                public void doSelect() {
                    reportMapper.getAllReport();
                }
            });

        session.commit();
        session.close();
        return DataJSON.reports2js(reports,page,total);
    }

    public void  removeReport(Integer reportId) throws IOException {
        String resource = "SqlMapConfig.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = factory.openSession(false);
        ReportMapper reportMapper = session.getMapper(ReportMapper.class);
        try {
            reportMapper.deleteReportById(reportId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String  banReport(boolean banUser,boolean banData,int reportId) throws IOException{
        String resource = "SqlMapConfig.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = factory.openSession(false);
        ReportMapper reportMapper = session.getMapper(ReportMapper.class);
        Report report = reportMapper.getReportById(reportId);
        reportMapper.deleteReportById(reportId);
        String dataId = report.getDataId();
        String userId = report.getReportedId();
        if(banData){
            DataMapper dataMapper = session.getMapper(DataMapper.class);
            DataInfoMapper dataInfoMapper = session.getMapper(DataInfoMapper.class);
            dataMapper.setInvalid(dataId);
            dataInfoMapper.deleteDataInfoByDataId(dataId);
        }
        if(banUser){
            UserMapper userMapper =session.getMapper(UserMapper.class);
            userMapper.setInvalid(userId);
        }

        session.commit();
        session.close();
        return "done";
    }
}
