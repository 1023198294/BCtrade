package com.example.demo.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.model.*;

import java.util.List;

public class DataJSON {
    public static JSONObject dataInfo2js(List<DataInfo> dataInfoList, int curPage, long totalPage){
        JSONObject result = new JSONObject();
        result.put("cur",curPage);
        result.put("page",totalPage);

        //    String dataid;
        //    String dataname;
        //    String size;
        //    String description;
        //    String key;
        for(int id = 0;id<dataInfoList.size();id++){
            JSONObject dataInfoJSON = new JSONObject();
            dataInfoJSON.put("id",id);
            dataInfoJSON.put("dataid",dataInfoList.get(id).getDataid());
            dataInfoJSON.put("dataname",dataInfoList.get(id).getDataname());
            dataInfoJSON.put("size",dataInfoList.get(id).getSize());
            dataInfoJSON.put("description",dataInfoList.get(id).getDescription());
            dataInfoJSON.put("value",dataInfoList.get(id).getValue());
            result.put("data"+id,dataInfoJSON);
        }
        return result;
    }

    public static JSONObject dataAsset2js(List<DataAsset> dataAssetList,int curPage, long totalPage){
        JSONObject result = new JSONObject();
        result.put("cur",curPage);
        result.put("page",totalPage);
        //    String dataid;
        //    String dataname;
        //    String size;
        //    String description;
        //    String key;
        for(int id = 0;id<dataAssetList.size();id++){
            //    public DataAsset(String dataId, String ownerId, String creatorId, String originalId, String value, String createdDate, String scratch) {
            JSONObject dataAssetJson = new JSONObject();
            dataAssetJson.put("id",id);
            dataAssetJson.put("dataid",dataAssetList.get(id).getDataId());
            dataAssetJson.put("creatorId",dataAssetList.get(id).getCreatorId());
            dataAssetJson.put("originalId",dataAssetList.get(id).getOriginalId());
            dataAssetJson.put("value",dataAssetList.get(id).getValue());
            dataAssetJson.put("createdDate",dataAssetList.get(id).getCreatedDate());
            //dataAssetJson.put("value",dataInfoList.get(id).getValue());
            result.put("data"+id,dataAssetJson);
        }
        return result;
    }

    public static JSONObject singleDataInfo2js(DataInfo dataInfo){
        JSONObject result = new JSONObject();
        result.put("dataname",dataInfo.getDataname());
        result.put("description",dataInfo.getDescription());
        return result;
    }

    public static JSONObject reports2js(List<Report> reportList,int curPage, long totalPage){
        JSONObject result = new JSONObject();
        result.put("cur",curPage);
        result.put("page",totalPage);
        for(int id = 0;id<reportList.size();id++){
            JSONObject reportJson = new JSONObject();
            reportJson.put("id",id);
            reportJson.put("reportId",reportList.get(id).getReportId());
            reportJson.put("type",reportList.get(id).getType());
            reportJson.put("reporterId",reportList.get(id).getReporterId());
            reportJson.put("reportedId",reportList.get(id).getReportedId());
            reportJson.put("dataId",reportList.get(id).getDataId());
            reportJson.put("description",reportList.get(id).getDescription());
            result.put("data"+id,reportJson);
        }
        return result;
    }
    public static JSONObject alarm2js(List<ChargeAlarm> alarms,int curPage,long totalPage){
        JSONObject result = new JSONObject();
        result.put("cur",curPage);
        result.put("page",totalPage);
        for(int id = 0;id<alarms.size();id++){
            JSONObject alarmJson = new JSONObject();
            alarmJson.put("id",id);
//            String userId;
//            String action;
//            String value;
            alarmJson.put("userId",alarms.get(id).getUserId());
            alarmJson.put("action",alarms.get(id).getAction());
            alarmJson.put("value",alarms.get(id).getValue());

            result.put("data"+id,alarmJson);
        }
        return result;
    }
    public static JSONObject trade2js(List<TradeInfo> tradeInfos, int curPage, long totalPage){
        JSONObject result = new JSONObject();
        result.put("cur",curPage);
        result.put("page",totalPage);
        for(int id = 0;id<tradeInfos.size();id++){
            JSONObject tradeJson = new JSONObject();
            tradeJson.put("id",id);
//            Integer id;
//            String dataId;
//            String fromId;
//            String toId;
//            String creatorId;
//            Double rate;//支付rate%作为版权费用
//            String value;
            tradeJson.put("dataId",tradeInfos.get(id).getDataId());
            tradeJson.put("fromId",tradeInfos.get(id).getFromId());
            tradeJson.put("toId",tradeInfos.get(id).getFromId());
            tradeJson.put("creatorId",tradeInfos.get(id).getCreatorId());
            tradeJson.put("rate",tradeInfos.get(id).getRate());
            tradeJson.put("value",tradeInfos.get(id).getValue());
            result.put("data"+id,tradeJson);
        }
        return result;
    }
}
