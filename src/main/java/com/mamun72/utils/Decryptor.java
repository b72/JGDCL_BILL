package com.mamun72.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Decryptor {

    Logger logger = LoggerFactory.getLogger(Decryptor.class);

    private static final String encryptionKey = "MAKV2SPBNI657328B";

    private static final byte[] salt = new byte[]{
            0x49, 0x76, 0x61, 0x6e, 0x20, 0x4d, 0x65, 0x64, 0x76, 0x65, 0x64, 0x65, 0x76
    };

    public String decrypt(String encryptedString) {
        try {

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            PBEKeySpec pbeKeySpec = new PBEKeySpec(encryptionKey.toCharArray(), salt, 1000, 384);

            byte[] derivedData = factory.generateSecret(pbeKeySpec).getEncoded();
            byte[] key = new byte[32];
            byte[] iv = new byte[16];

            System.arraycopy(derivedData, 0, key, 0, key.length);

            System.arraycopy(derivedData, key.length, iv, 0, iv.length);

            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);

            byte[] result = cipher.doFinal(Base64.getDecoder().decode(encryptedString));

            return new String(result, StandardCharsets.UTF_16LE);

        } catch (Exception e) {
            logger.error("Decryption error " + e.getMessage() +" - invoked " + new Throwable().getStackTrace()[0].getMethodName());
            return null;
        }
    }
}
