package com.example.demo;

import com.example.demo.service.blockchain.MyBlockChainService;
import com.example.demo.utils.DataUtils;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric_ca.sdk.exception.EnrollmentException;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddAccountTest {
    private String dataId = "";
    @Test
    public void enrollAdminTest() throws Exception{
        MyBlockChainService myBlockChainService = new MyBlockChainService();
        myBlockChainService.enrollAdmin();
    }

    @Test
    public void registerTest() throws Exception {
        MyBlockChainService myBlockChainService = new MyBlockChainService();
        myBlockChainService.registerUser("HYJ");
        myBlockChainService.registerUser("HZL");
    }

    @Test
    public void addWalletTest() throws Exception {
        MyBlockChainService myBlockChainService = new MyBlockChainService();
        myBlockChainService.createWallet("HYJ", 2.0);
        myBlockChainService.createWallet("HZL",4.0);
    }
    @Test
    public void prerequisite() throws Exception{
        MyBlockChainService myBlockChainService = new MyBlockChainService();
        myBlockChainService.enrollAdmin();
        myBlockChainService.registerUser("HYJ");
        myBlockChainService.registerUser("HZL");
        myBlockChainService.createWallet("HYJ", 2.0);
        myBlockChainService.createWallet("HZL",4.0);
        /*
        this.dataId = DataUtils.generateShortUuid();
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createdDate = formatter.format(currentTime);
        myBlockChainService.addData(dataId,"HYJ","HYJ",dataId,1.0,createdDate,"noms");
        */
    }
    @Test
    public void chargeDrawTest() throws Exception{
        MyBlockChainService myBlockChainService = new MyBlockChainService();
        myBlockChainService.chargeOrDraw("HYJ",1.5);
        myBlockChainService.chargeOrDraw("HYJ",-1);
    }
    @Test
    public void addDataTest() throws Exception {
        MyBlockChainService myBlockChainService = new MyBlockChainService();
        this.dataId = DataUtils.generateShortUuid();
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createdDate = formatter.format(currentTime);
        myBlockChainService.addData(this.dataId,"HYJ","HYJ",this.dataId,1.0,createdDate,"noms");
        File file = new File("test.dat");
        FileOutputStream out;
        try{
            out = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
            objectOutputStream.writeObject(this.dataId);
            objectOutputStream.flush();
            objectOutputStream.close();
            System.out.println("write dataIdOK");
        }catch (Exception e){
            System.out.println("write dataId failed");
            e.printStackTrace();
        }
    }

    @Test
    public void tradeInfoTest() throws Exception{
        MyBlockChainService myBlockChainService = new MyBlockChainService();
        String newDataId = DataUtils.generateShortUuid();
        File file = new File("test.dat");
        FileInputStream in;
        Object temp = null;
        try{
            in = new FileInputStream(file);
            ObjectInputStream objIn = new ObjectInputStream(in);
            temp = objIn.readObject();
            objIn.close();
            System.out.println("readSuccess");
        }catch (Exception e){
            System.out.println("read failed");
            e.printStackTrace();
        }
        assert temp != null;
        this.dataId = temp.toString();
        System.out.println(dataId);
        myBlockChainService.addTradeInfo("HYJ","HZL",newDataId,this.dataId,"HYJ",0.5,1.0);
    }




}
