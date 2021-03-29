package com.example.demo;

import com.example.demo.utils.AESUtil;
import com.example.demo.utils.RSAUtil;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.io.File;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class TestCipher {
    @Test
    public void tesRSA() throws Exception{
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
    @Test
    public void testAES() throws Exception{
        String content = "Hello world";
        String key = "114514";
        SecretKey secretKey = AESUtil.generateKey(key.getBytes());
        byte[] sk = secretKey.getEncoded();
        byte[] cipherBytes = AESUtil.encrypt(content.getBytes(),sk);
        byte[] plainBytes = AESUtil.decrypt(cipherBytes,sk);
        System.out.println("明文是:" + content);
        System.out.println("加密后是:" + new String(cipherBytes));
        System.out.println("解密后是:" + new String(plainBytes));
        String fileName = "demo-0.0.1-SNAPSHOT.jar";
        AESUtil.encryptFile(new File("./file/"+fileName),new File("./file/"+fileName+"_ec"),sk);
        AESUtil.decryptFile(new File("./file/"+fileName+"_ec"),new File("./file/new_"+fileName),sk);
    }
}
