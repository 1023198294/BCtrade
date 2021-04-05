package com.example.demo.dao;

import com.example.demo.model.Report;

import java.util.List;

public interface ReportMapper {
    public void insertReport(Report report);
    public Report getReportById(Integer reportId);
    public List<Report> getAllReport();
    public void deleteReportById(Integer reportId);
}
