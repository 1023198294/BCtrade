package com.example.demo.model;

public class Report{
    Integer reportId;
    String type;
    String reporterId;
    String reportedId;
    String dataId;
    String description;

    public Report(Integer reportId, String type, String reporterId, String reportedId, String dataId, String description) {
        this.reportId = reportId;
        this.type = type;
        this.reporterId = reporterId;
        this.reportedId = reportedId;
        this.dataId = dataId;
        this.description = description;
    }

    public Integer getReportId() {
        return reportId;
    }

    public String getType() {
        return type;
    }

    public String getReporterId() {
        return reporterId;
    }

    public String getReportedId() {
        return reportedId;
    }

    public String getDataId() {
        return dataId;
    }

    public String getDescription() {
        return description;
    }

}
