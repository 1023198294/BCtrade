package com.example.demo.dao;

import com.example.demo.model.DataInfo;

import java.util.List;

public interface DataInfoMapper {
    public void insertDataInfo(DataInfo dataInfo);
    //String dataid, String dataname, String size, String description, String key
    public DataInfo getDataInfoById(String dataid);
    public List<DataInfo> getDataInfoByLikeName(String dname);
    public void deleteDataInfoByDataId(String dataid);
}
