package org.bctrade.code;

import com.owlike.genson.Genson;
import com.sun.net.httpserver.Filter;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.contract.annotation.Contact;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.License;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Contract(
        name = "bctrade",
        info = @Info(
                title = "BCTrade Contract",
                description = "A hyperlegendary traede station",
                version = "0.0.1-SNAPSHOT",
                license = @License(
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"),
                contact = @Contact(
                        email = "1023198294@qq.com",
                        name = "yangjun",
                        url = "https://github.com/1023198294")

        )
)

@Default
public final class BCTrade implements ContractInterface {
    private final Genson genson = new Genson();
    private enum Errors{
        DATA_SOURCE_NOT_FOUND,
        TOKEN_NOT_ENOUGH,
        WALLET_NOT_FOUND,
        WALLET_ALREADY_EXISTS
    }

    @Transaction
    public String queryWallet(final Context ctx,final String key){
        ChaincodeStub stub = ctx.getStub();
        String walletState = stub.getStringState(key);
        if(walletState.isEmpty()){
            String errorMessage = String.format("User %s does not exist", key);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, Errors.WALLET_NOT_FOUND.toString());
        }

        return walletState;
    }

    @Transaction
    public String createWallet(final Context ctx,final String key,final double deposit){
        ChaincodeStub stub = ctx.getStub();
        String walletState = stub.getStringState(key);
        if (!walletState.isEmpty()) {
            String errorMessage = String.format("User %s already has a wallet", key);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, Errors.WALLET_ALREADY_EXISTS.toString());
        }

        stub.putStringState(key,String.valueOf(deposit));
        return "success";
    }
    @Transaction
    public String addData(final Context ctx,
                          String dataId,
                          String ownerId,
                          String creatorId,
                          String originalDataId,
                          double value,
                          String createdDate,
                          String scratch){
        ChaincodeStub stub = ctx.getStub();
        //String dataId, String ownerId, String creatorId, String originalDataId, double value, String createdDate, String scratch
        String ownerState = stub.getStringState(ownerId);
        String creatorState = stub.getStringState(creatorId);
        if(ownerState.isEmpty()){
            String errorMessage = String.format("owner %s does not exist",ownerId);
            throw new ChaincodeException(errorMessage,Errors.WALLET_NOT_FOUND.toString());
        }

        if(creatorState.isEmpty()){
            String errorMessage = String.format("Creator %s does not exist",creatorId);
            throw new ChaincodeException(errorMessage,Errors.WALLET_NOT_FOUND.toString());
        }
        if(originalDataId.isEmpty()){
            originalDataId = dataId;
        }
        DataInfo dataInfo = new DataInfo(dataId,ownerId,creatorId,originalDataId,value,createdDate,scratch);
        String data = genson.serialize(dataInfo);
        stub.putStringState(dataId, data);
        return "success dataInfo:\n"+data+"\n";
    }
    @Transaction
    public String addTradeInfo(final Context ctx,String fromId,String toId,String dataId,String originalDataId,String creatorId,double rate,double value){
        ChaincodeStub stub = ctx.getStub();
        String fromState = stub.getStringState(fromId);
        String toState = stub.getStringState(toId);
        String creatorState =stub.getStringState(creatorId);
        String dataState = stub.getStringState(originalDataId);
        if (dataState.isEmpty()) {
            String errorMessage = String.format("data %s does not exist", originalDataId);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, Errors.DATA_SOURCE_NOT_FOUND.toString());
        }
        if(fromState.isEmpty()||toState.isEmpty()||creatorState.isEmpty()){
            String errorMessage = String.format("userId %s or %s or %s do not exist", fromId,toId,originalDataId);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, Errors.WALLET_NOT_FOUND.toString());
        }
        double fromToken = Double.parseDouble(fromState);
        double toToken= Double.parseDouble(toState);
        double creatorToken = Double.parseDouble(creatorState);

        if(toToken<value){
            String errorMessage = String.format("User %s's Money not enough",toId);
            throw new ChaincodeException(errorMessage,Errors.TOKEN_NOT_ENOUGH.toString());
        }
        //return "success "+fromToken;
        toToken -= value;
        if(!fromId.equals(creatorId)){
            fromToken += value*(1-rate);
            creatorToken += value*rate;
            stub.putStringState(creatorId,String.valueOf(creatorToken));
        }else{
            fromToken  += value;
        }

        stub.putStringState(fromId,String.valueOf(fromToken));
        stub.putStringState(toId,String.valueOf(toToken));
        //return "success/n"+fromId+":"+fromToken+" & "+toId+":"+toToken+" & "+creatorId+":"+creatorToken+"\n";

        DataInfo dataInfo= genson.deserialize(dataState,DataInfo.class);
        DataInfo newDataInfo = new DataInfo(dataId,toId,creatorId,dataInfo.getOriginalDataId(),value,dataInfo.getCreatedDate(),dataInfo.getScratch());
        stub.putStringState(dataId,genson.serialize(newDataInfo));
        return "success/n"+fromId+":"+fromToken+" & "+toId+":"+toToken+" & "+creatorId+":"+creatorToken+"\n";
        //return "success";*/
    }

    @Transaction
    public String chargeOrDraw(final Context ctx,String userId,double value){
        ChaincodeStub stub = ctx.getStub();

        String userState = stub.getStringState(userId);
        if(userState.isEmpty()){
            String errorMessage = String.format("userId %s do not exist", userId);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, Errors.WALLET_NOT_FOUND.toString());
        }
        double userToken = Double.parseDouble(userState);
        if(value<0&&userToken+value<0){
            String errorMessage = String.format("requires %f but only %f remains in %s's account",value,userToken, userId);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, Errors.TOKEN_NOT_ENOUGH.toString());
        }
        userToken = userToken+value;
        stub.putStringState(userId,String.valueOf(userToken));
        return userId+":"+userToken;
    }

    @Transaction
    public List<String> queryAllWallets(final Context ctx, List<String> keys){
        ChaincodeStub stub = ctx.getStub();
        List<String> ret = new ArrayList<>();
        for(String key:keys){
            String walletState = stub.getStringState(key);
            if (walletState.isEmpty()) {
                String errorMessage = String.format("User %s does not exist has a wallet", key);
                System.out.println(errorMessage);
                throw new ChaincodeException(errorMessage, Errors.WALLET_NOT_FOUND.toString());
            }
            ret.add(walletState);
        }
        return ret;
    }



    @Transaction
    public void initLedger(final Context ctx){
        ChaincodeStub stub = ctx.getStub();
        stub.putStringState("admin","HYJ");
    }
}
