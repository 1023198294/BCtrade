package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.model.DataAsset;
import com.example.demo.model.Report;
import com.example.demo.service.DataService;
import com.example.demo.service.ReportService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("admin/")
@CrossOrigin
public class AdministratorController {
    @RequestMapping("report")
    public String report(String type,String dataid,String description){
    //type, reporterid,reportedid, dataid,description
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        String reporterId = (String) session.getAttribute("userId");
        DataService dataService = new DataService();
        DataAsset dataAsset;
        try{
            dataAsset = dataService.getDataIdByDataId(dataid);
        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        }
        String reportedid = dataAsset.getOwnerId();
        Report report = new Report(0,type,reporterId,reportedid,dataid,description);
        ReportService reportService = new ReportService();
        try{
            reportService.insertReport(report);
            return "done";
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }
    @RequestMapping("getReportList")
    public String getReports(String page,String pageSize){
        Subject subject = SecurityUtils.getSubject();
        ReportService reportService = new ReportService();
        int pageInt = Integer.parseInt(page);
        int pageSizeInt = Integer.parseInt(pageSize);
        if(subject.hasRole("admin")){
            try{
                JSONObject reportList = reportService.getAllReport(pageInt,pageSizeInt);

                return reportList.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
        }else{
            return "please login with an admin account";
        }
    }

    @RequestMapping("getCA")
    public String getCA(String page,String pageSize){
        Subject subject = SecurityUtils.getSubject();
        ReportService reportService = new ReportService();
        int pageInt = Integer.parseInt(page);
        int pageSizeInt = Integer.parseInt(pageSize);
        if(subject.hasRole("admin")){
            try{
                JSONObject reportList = reportService.getChargeAlarms(pageInt,pageSizeInt);
                return reportList.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
        }else{
            return "please login with an admin account";
        }
    }

    @RequestMapping("get_money_involved")
    public String moneyInvolved(String dataId){
        Subject subject = SecurityUtils.getSubject();
        ReportService reportService = new ReportService();
        if(subject.hasRole("admin")){
            try{
                JSONObject reportList = reportService.getMoneyInvolved(dataId);
                return reportList.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
        }else{
            return "please login with an admin account";
        }
    }

    @RequestMapping("get_user_trades")
    public String getUT(String userId,String page,String pageSize){
        Subject subject = SecurityUtils.getSubject();
        ReportService reportService = new ReportService();
        int pageInt = Integer.parseInt(page);
        int pageSizeInt = Integer.parseInt(pageSize);
        if(subject.hasRole("admin")){
            try{
                JSONObject reportList = reportService.getTradeInfoById(userId,pageInt,pageSizeInt);
                return reportList.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
        }else{
            return "please login with an admin account";
        }
    }

    @RequestMapping("get_data_trades")
    public String getDT(String dataId,String page,String pageSize){
        Subject subject = SecurityUtils.getSubject();
        ReportService reportService = new ReportService();
        int pageInt = Integer.parseInt(page);
        int pageSizeInt = Integer.parseInt(pageSize);
        if(subject.hasRole("admin")){
            try{
                JSONObject reportList = reportService.getTradeInfoByDataId(dataId,pageInt,pageSizeInt);
                return reportList.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
        }else{
            return "please login with an admin account";
        }
    }

    @RequestMapping("get_all_trades")
    public String getAT(String page,String pageSize){
        Subject subject = SecurityUtils.getSubject();
        ReportService reportService = new ReportService();
        int pageInt = Integer.parseInt(page);
        int pageSizeInt = Integer.parseInt(pageSize);
        if(subject.hasRole("admin")){
            try{
                JSONObject reportList = reportService.getTradeInfoAll(pageInt,pageSizeInt);
                return reportList.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
        }else{
            return "please login with an admin account";
        }
    }
    @RequestMapping("ban")
    public String ban(int reportId,boolean banUser,boolean banData){
        ReportService reportService = new ReportService();
        try {
            return reportService.banReport(banUser,banData,reportId);
        } catch (IOException e) {
            e.printStackTrace();
            return "ban error";
        }
    }

    @RequestMapping("call_the_police")
    public String ctp(){
        return "已将数据上报公安";
    }
}
