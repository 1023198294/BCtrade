package com.example.demo.dao;

import com.example.demo.model.DataContent;

public interface DataContentMapper {
    public void insertDataContent(DataContent dataContent);
    public DataContent getDataContentById(String dataid);
    public String getDataRealNameById(String dataid);
}
