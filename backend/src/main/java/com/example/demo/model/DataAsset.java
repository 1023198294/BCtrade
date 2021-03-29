package com.example.demo.model;

import java.util.Date;

public class DataAsset {
    String dataId;
    String ownerId;
    String creatorId;
    String originalId;
    String value;
    String createdDate;
    String scratch;


    public DataAsset(String dataId, String ownerId, String creatorId, String originalId, String value, String createdDate, String scratch) {
        this.dataId = dataId;
        this.ownerId = ownerId;
        this.creatorId = creatorId;
        this.originalId = originalId;
        this.value = value;
        this.createdDate = createdDate;
        this.scratch = scratch;
    }

    @Override
    public String toString() {
        return "DataAsset{" +
                "dataId='" + dataId + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", creatorId='" + creatorId + '\'' +
                ", originalId='" + originalId + '\'' +
                ", value='" + value + '\'' +
                ", createdDate=" + createdDate +
                ", scratch='" + scratch + '\'' +
                '}';
    }

    public String getDataId() {
        return dataId;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public String getOriginalId() {
        return originalId;
    }

    public String getValue() {
        return value;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getScratch() {
        return scratch;
    }

    public String getOwnerId() {
        return ownerId;
    }
}
