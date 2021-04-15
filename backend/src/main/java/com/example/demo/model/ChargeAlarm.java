package com.example.demo.model;

public class ChargeAlarm {
    Integer id;
    String userId;
    String action;
    String value;
//### Cause: org.apache.ibatis.executor.ExecutorException: No constructor found in com.example.demo.model.ChargeAlarm matching [java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Double, java.lang.String]] with root cause
    public ChargeAlarm(Integer id,String userId, String action, String value) {
        this.id = id;
        this.userId = userId;
        this.action = action;
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getAction() {
        return action;
    }

    public String getValue() {
        return value;
    }
}
