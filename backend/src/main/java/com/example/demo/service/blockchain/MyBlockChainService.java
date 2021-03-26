package com.example.demo.service.blockchain;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import org.hyperledger.fabric.gateway.*;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric.sdk.security.CryptoSuiteFactory;
import org.hyperledger.fabric_ca.sdk.EnrollmentRequest;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.hyperledger.fabric_ca.sdk.exception.EnrollmentException;

public class MyBlockChainService {
    static {
        System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
    }



    public String createWallet(String userId,Double value) throws Exception{
        Path walletPath = Paths.get("wallet");
        Wallet wallet = Wallets.newFileSystemWallet(walletPath);
        // load a CCP
        Path networkConfigPath = Paths.get("..", "fabric-samples", "test-network", "organizations", "peerOrganizations", "org1.example.com", "connection-org1.yaml");
        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, userId).networkConfig(networkConfigPath).discovery(true);

        try(Gateway gateway = builder.connect()){
            Network network = gateway.getNetwork("mychannel");
            Contract contract = network.getContract("bctrade");
            byte[] result;
            //result = contract.evaluateTransaction("createWallet");
            //System.out.println(new String(result));
            result = contract.submitTransaction("createWallet",userId,value.toString());
            System.out.println(new String(result));
            result = contract.submitTransaction("queryWallet",userId);
            System.out.println(new String(result));
        } catch (ContractException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }
        return "success";
    }

    public String addData(String dataId, String ownerId, String creatorId, String originalDataId, double value, String createdDate, String scratch) throws Exception{
        Path walletPath = Paths.get("wallet");
        Wallet wallet = Wallets.newFileSystemWallet(walletPath);
        // load a CCP
        Path networkConfigPath = Paths.get("..", "fabric-samples", "test-network", "organizations", "peerOrganizations", "org1.example.com", "connection-org1.yaml");
        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, ownerId).networkConfig(networkConfigPath).discovery(true);
        byte[] result = new byte[0];
        try(Gateway gateway = builder.connect()){
            Network network = gateway.getNetwork("mychannel");
            Contract contract = network.getContract("bctrade");
            result = contract.submitTransaction("addData",dataId,ownerId,creatorId,originalDataId,String.valueOf(value),createdDate,scratch);
            System.out.println(new String(result));
        } catch (ContractException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }
        return new String(result);
    }

    public String addTradeInfo(String fromId,String toId,String dataId,String originalDataId,String creatorId,double rate,double value)throws Exception{
        Path walletPath = Paths.get("wallet");
        Wallet wallet = Wallets.newFileSystemWallet(walletPath);
        // load a CCP
        Path networkConfigPath = Paths.get("..", "fabric-samples", "test-network", "organizations", "peerOrganizations", "org1.example.com", "connection-org1.yaml");
        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, fromId).networkConfig(networkConfigPath).discovery(true);
        byte[] result = new byte[0];
        try(Gateway gateway = builder.connect()){
            Network network = gateway.getNetwork("mychannel");
            Contract contract = network.getContract("bctrade");
            result = contract.submitTransaction("addTradeInfo",fromId,toId,dataId,originalDataId,creatorId,String.valueOf(rate),String.valueOf(value));
            //addTradeInfo(final Context ctx,String fromId,String toId,String dataId,String originalDataId,String creatorId,double rate,double value){
            System.out.println(new String(result));
        } catch (ContractException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }
        return new String(result);
    }
    public String enrollAdmin() throws Exception {
        // Create a CA client for interacting with the CA.
        Properties props = new Properties();
        props.put("pemFile",
                "../fabric-samples/test-network/organizations/peerOrganizations/org1.example.com/ca/ca.org1.example.com-cert.pem");
        props.put("allowAllHostNames", "true");
        HFCAClient caClient = HFCAClient.createNewInstance("https://localhost:7054", props);
        CryptoSuite cryptoSuite = CryptoSuiteFactory.getDefault().getCryptoSuite();
        caClient.setCryptoSuite(cryptoSuite);

        // Create a wallet for managing identities
        Wallet wallet = Wallets.newFileSystemWallet(Paths.get("wallet"));

        // Check to see if we've already enrolled the admin user.
        if (wallet.get("admin") != null) {
            System.out.println("An identity for the admin user \"admin\" already exists in the wallet");
            return "failed";
        }

        // Enroll the admin user, and import the new identity into the wallet.
        final EnrollmentRequest enrollmentRequestTLS = new EnrollmentRequest();
        enrollmentRequestTLS.addHost("localhost");
        enrollmentRequestTLS.setProfile("tls");
        Enrollment enrollment = caClient.enroll("admin", "adminpw", enrollmentRequestTLS);
        Identity user = Identities.newX509Identity("Org1MSP", enrollment);
        wallet.put("admin", user);
        System.out.println("Successfully enrolled user \"admin\" and imported it into the wallet");
        return "success";
        }
    public String registerUser(String userId)throws Exception{
        Properties props = new Properties();
        props.put("pemFile",
                "../fabric-samples/test-network/organizations/peerOrganizations/org1.example.com/ca/ca.org1.example.com-cert.pem");
        props.put("allowAllHostNames", "true");
        HFCAClient caClient = HFCAClient.createNewInstance("https://localhost:7054", props);
        CryptoSuite cryptoSuite = CryptoSuiteFactory.getDefault().getCryptoSuite();
        caClient.setCryptoSuite(cryptoSuite);

        // Create a wallet for managing identities
        Wallet wallet = Wallets.newFileSystemWallet(Paths.get("wallet"));
        if (wallet.get(userId) != null) {
            System.out.println("An identity for the user\" "+userId+" \"already exists in the wallet");
            return "An identity for the user \""+userId+"\" already exists in the wallet";
        }
        X509Identity adminIdentity = (X509Identity)wallet.get("admin");
        if (adminIdentity == null) {
            System.out.println("\"admin\" needs to be enrolled and added to the wallet first");
            return "\"admin\" needs to be enrolled and added to the wallet first";
        }

        User admin = new User() {

            @Override
            public String getName() {
                return "admin";
            }

            @Override
            public Set<String> getRoles() {
                return null;
            }

            @Override
            public String getAccount() {
                return null;
            }

            @Override
            public String getAffiliation() {
                return "org1.department1";
            }

            @Override
            public Enrollment getEnrollment() {
                return new Enrollment() {

                    @Override
                    public PrivateKey getKey() {
                        return adminIdentity.getPrivateKey();
                    }

                    @Override
                    public String getCert() {
                        return Identities.toPemString(adminIdentity.getCertificate());
                    }
                };
            }

            @Override
            public String getMspId() {
                return "Org1MSP";
            }

        };
        RegistrationRequest registrationRequest = new RegistrationRequest(userId);
        registrationRequest.setAffiliation("org1.department1");
        registrationRequest.setEnrollmentID(userId);
        String enrollmentSecret = caClient.register(registrationRequest, admin);
        Enrollment enrollment = caClient.enroll(userId, enrollmentSecret);
        Identity user = Identities.newX509Identity("Org1MSP", adminIdentity.getCertificate(), adminIdentity.getPrivateKey());
        wallet.put(userId, user);
        System.out.println("Successfully enrolled user \" "+userId+ " \" and imported it into the wallet");
        return "success";
    }
    public String chargeOrDraw(String userId,double value) throws Exception{
        //chargeOrDraw(final Context ctx,String userId,double value)
        Path walletPath = Paths.get("wallet");
        Wallet wallet = Wallets.newFileSystemWallet(walletPath);
        // load a CCP
        Path networkConfigPath = Paths.get("..", "fabric-samples", "test-network", "organizations", "peerOrganizations", "org1.example.com", "connection-org1.yaml");
        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, userId).networkConfig(networkConfigPath).discovery(true);
        byte[] result = new byte[0];
        try(Gateway gateway = builder.connect()){
            Network network = gateway.getNetwork("mychannel");
            Contract contract = network.getContract("bctrade");
            result = contract.submitTransaction("chargeOrDraw",userId,String.valueOf(value));
            System.out.println(new String(result));
        } catch (ContractException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }
        return new String(result);
    }

    public String queryWalletById(String userId) throws Exception{
        Path walletPath = Paths.get("wallet");
        Wallet wallet = Wallets.newFileSystemWallet(walletPath);
        // load a CCP
        Path networkConfigPath = Paths.get("..", "fabric-samples", "test-network", "organizations", "peerOrganizations", "org1.example.com", "connection-org1.yaml");
        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, userId).networkConfig(networkConfigPath).discovery(true);
        byte[] result = new byte[0];
        try(Gateway gateway = builder.connect()){
            Network network = gateway.getNetwork("mychannel");
            Contract contract = network.getContract("bctrade");
            result = contract.submitTransaction("queryWallet",userId);
            System.out.println(new String(result));
        } catch (ContractException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }
        return new String(result);
    }
}
