package com.example.demo.model;

public class TradeInfo {
    Integer id;
    String dataId;
    String fromId;
    String toId;
    String creatorId;
    Double rate;//支付rate%作为版权费用
    String value;

    public Integer getId() {
        return id;
    }

    public String getDataId() {
        return dataId;
    }

    public String getFromId() {
        return fromId;
    }

    public String getToId() {
        return toId;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public Double getRate() {
        return rate;
    }

    public String getValue() {
        return value;
    }

    public TradeInfo(Integer id,String dataId, String fromId, String toId, String creatorId, Double rate, String value) {
        this.id = id;
        this.dataId = dataId;
        this.fromId = fromId;
        this.toId = toId;
        this.creatorId = creatorId;
        this.rate = rate;
        this.value = value;
    }

    @Override
    public String toString() {
        return "TradeInfo{" +
                "id=" + id +
                ", dataId='" + dataId + '\'' +
                ", fromId='" + fromId + '\'' +
                ", toId='" + toId + '\'' +
                ", creatorId='" + creatorId + '\'' +
                ", rate=" + rate +
                ", value='" + value + '\'' +
                '}';
    }
}
