package com.example.demo.dao;

import com.example.demo.model.DataInfo;

public interface DataInfoMapper {
    public void insertDataInfo(DataInfo dataInfo);
    //String dataid, String dataname, String size, String description, String key
    public DataInfo getDataInfoById(String dataid);
}
