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
}
