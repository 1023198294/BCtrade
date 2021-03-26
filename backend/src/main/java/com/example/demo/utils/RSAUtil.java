package com.example.demo.utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class RSAUtil {
    public byte[] encrypt(RSAPublicKey publicKey,byte[] srcBytes) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        if(publicKey!=null){
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE,publicKey);
            byte[] resultBytes = cipher.doFinal(srcBytes);
            return resultBytes;
        }
        return null;
    }
    public byte[] decrypt(RSAPrivateKey privateKey,byte[] srcBytes) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        if(privateKey!=null){
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE,privateKey);
            byte[] resultBytes =cipher.doFinal(srcBytes);
            return resultBytes;
        }
        return null;
    }
}
