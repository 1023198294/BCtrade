package com.example.demo.dao;

import com.example.demo.model.ChargeAlarm;

import java.util.List;

public interface ChargeAlarmMapper {
    public void insertChargeAlarm(ChargeAlarm chargeAlarm);
    public List<ChargeAlarm> getAlarmByUserId(String userId);
    public List<ChargeAlarm> getAlarmAll();
}
