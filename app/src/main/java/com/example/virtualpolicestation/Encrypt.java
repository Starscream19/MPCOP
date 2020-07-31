package com.example.virtualpolicestation;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Encrypt {
    public static byte[] encrypt(byte[] plaintext, SecretKey key) throws Exception
    {
        Cipher cipher = Cipher.getInstance("AES");

        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");


        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] cipherText = cipher.doFinal(plaintext);
        return cipherText;
    }
}


