package com.example.demo.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.model.DataAsset;
import com.example.demo.model.DataInfo;

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
}
