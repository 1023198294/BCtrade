package com.example.demo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.*;
import com.example.demo.model.ChargeAlarm;
import com.example.demo.model.Report;
import com.example.demo.model.TradeInfo;
import com.example.demo.utils.DataJSON;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.google.api.Page;
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
    public JSONObject getTradeInfoById(String userId,int page,int pageSize) throws IOException{
        String resource = "SqlMapConfig.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = factory.openSession(false);
        TradeInfoMapper tradeInfoMapper = session.getMapper(TradeInfoMapper.class);
        List<TradeInfo> tradeInfos;
        PageHelper.startPage(page,pageSize);
        tradeInfos = tradeInfoMapper.getTradeInfoByUserId(userId);
        long total = PageHelper.count(new ISelect() {
            @Override
            public void doSelect() {
                tradeInfoMapper.getTradeInfoByUserId(userId);
            }
        });
        session.commit();
        session.close();
        return DataJSON.trade2js(tradeInfos,page,total);
    }
    public JSONObject getTradeInfoByDataId(String dataId,int page,int pageSize) throws IOException{
        String resource = "SqlMapConfig.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = factory.openSession(false);
        TradeInfoMapper tradeInfoMapper = session.getMapper(TradeInfoMapper.class);
        List<TradeInfo> tradeInfos;
        PageHelper.startPage(page,pageSize);
        tradeInfos = tradeInfoMapper.getTradeInfoByDataId(dataId);
        long total = PageHelper.count(new ISelect() {
            @Override
            public void doSelect() {
                tradeInfoMapper.getTradeInfoByDataId(dataId);
            }
        });
        session.commit();
        session.close();
        return DataJSON.trade2js(tradeInfos,page,total);
    }


    public JSONObject getTradeInfoAll(int page,int pageSize) throws IOException{
        String resource = "SqlMapConfig.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = factory.openSession(false);
        TradeInfoMapper tradeInfoMapper = session.getMapper(TradeInfoMapper.class);
        List<TradeInfo> tradeInfos;
        PageHelper.startPage(page,pageSize);
        tradeInfos = tradeInfoMapper.getAllTradeInfo();
        long total = PageHelper.count(new ISelect() {
            @Override
            public void doSelect() {
                tradeInfoMapper.getAllTradeInfo();
            }
        });
        session.commit();
        session.close();
        return DataJSON.trade2js(tradeInfos,page,total);
    }

    public JSONObject getChargeAlarms(int page,int pageSize) throws IOException{
        String resource = "SqlMapConfig.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = factory.openSession(false);
        ChargeAlarmMapper chargeAlarmMapper = session.getMapper(ChargeAlarmMapper.class);
        List<ChargeAlarm> alarms;
        PageHelper.startPage(page,pageSize);
        alarms = chargeAlarmMapper.getAlarmAll();
        long total = PageHelper.count(new ISelect() {
            @Override
            public void doSelect() {
                chargeAlarmMapper.getAlarmAll();
            }
        });
        session.commit();
        session.close();
        return DataJSON.alarm2js(alarms,page,total);
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
        session.commit();
        session.close();
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

    public JSONObject getMoneyInvolved(String dataId) throws IOException {
        String resource = "SqlMapConfig.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = factory.openSession(false);

        TradeInfoMapper tradeInfoMapper = session.getMapper(TradeInfoMapper.class);
        List<TradeInfo> tradeInfos = tradeInfoMapper.getTradeInfoByDataId(dataId);
        JSONObject res = new JSONObject();
        double value = 0;
        int cases = 0;
        if(tradeInfos.size()>0){
            for (TradeInfo tradeInfo:tradeInfos){
                value+= Double.parseDouble(tradeInfo.getValue());
                cases+=1;
            }
        }
        res.put("value",value);
        res.put("cases",cases);
        session.commit();
        session.close();
        return  res;
    }
}
