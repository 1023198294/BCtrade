package com.example.demo.dao;

import com.example.demo.model.TradeInfo;

import java.util.List;

public interface TradeInfoMapper {
    public void insertTradeInfo(TradeInfo tradeInfo);
    public List<TradeInfo> getTradeInfoByUserId(String userId);
    public List<TradeInfo> getAllTradeInfo();
    public List<TradeInfo> getTradeInfoByDataId(String dataId);

}
