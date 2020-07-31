package com.example.virtualpolicestation;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Decrypt {
    public static String decrypt(byte[] cipherText, SecretKeySpec key)
    {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec keySpec = key;

            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decryptedText = cipher.doFinal(cipherText);
            return new String(decryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
