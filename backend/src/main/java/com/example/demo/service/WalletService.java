package com.example.demo.service;

import com.example.demo.model.ExCode;

public class WalletService {
    public double getDepositMoney(){
        return 0.0;
    }
    public ExCode chargeBCCoin(double amount){
        return ExCode.SUCCESS;
    }
    public ExCode drawBCCoin(double amount){
        return ExCode.SUCCESS;
    }
}
