package com.example.demo.model;

public class DataInfo {
    String dataid;
    String dataname;
    String size;
    String description;
    byte[] key;
    String value;
    Boolean available;
    public DataInfo(String dataid, String dataname, String size, String description,byte[] key,String value,Boolean available) {
        this.dataid = dataid;
        this.dataname = dataname;
        this.size = size;
        this.description = description;
        this.key = key;
        this.value = value;
        this.available = available;
    }

    public Boolean getAvailable() {
        return available;
    }

    @Override
    public String toString() {
        return "DataInfo{" +
                "dataid='" + dataid + '\'' +
                ", dataname='" + dataname + '\'' +
                ", size='" + size + '\'' +
                ", description='" + description + '\'' +
                ", key='" + key.toString() + '\'' +
                '}';
    }

    public String getDataid() {
        return dataid;
    }

    public String getDataname() {
        return dataname;
    }

    public String getSize() {
        return size;
    }

    public String getDescription() {
        return description;
    }

    public byte[] getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

}
