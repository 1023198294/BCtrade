package com.example.demo.model;

public class DataInfo {
    String dataid;
    String dataname;
    String size;
    String description;
    String key;

    public DataInfo(String dataid, String dataname, String size, String description, String key) {
        this.dataid = dataid;
        this.dataname = dataname;
        this.size = size;
        this.description = description;
        this.key = key;
    }

    @Override
    public String toString() {
        return "DataInfo{" +
                "dataid='" + dataid + '\'' +
                ", dataname='" + dataname + '\'' +
                ", size='" + size + '\'' +
                ", description='" + description + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
