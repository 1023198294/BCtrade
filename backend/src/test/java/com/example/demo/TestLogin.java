package com.example.demo;

import com.example.demo.utils.RSAUtil;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class TestLogin {
    @Test
    public void testCryptoGen() throws Exception{
        String msg = "我带你们打,wdnmd";
        RSAUtil rsaUtil = new RSAUtil();
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024);
        KeyPair keyPair = generator.generateKeyPair();

        RSAPublicKey publicKey= (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        byte[] srcBytes = msg.getBytes();
        byte[] resultBytes = rsaUtil.encrypt(publicKey,srcBytes);
        byte[] decBytes = rsaUtil.decrypt(privateKey,resultBytes);
        System.out.println("明文是:" + msg);
        System.out.println("加密后是:" + new String(resultBytes));
        System.out.println("解密后是:" + new String(decBytes));
    }
}
