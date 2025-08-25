package com.ruoyi.common.utils.ase;

import com.ruoyi.common.utils.url.UrlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class AESUtil {

    private static final Logger log = LoggerFactory.getLogger(AESUtil.class);

    private static final String ALGORITHM = "AES";
    private static final int KEY_SIZE = 128;

    public static String generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(KEY_SIZE, new SecureRandom());
        SecretKey secretKey = keyGenerator.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    public static String encrypt(String data) throws Exception {
        return encrypt(data, UrlUtil.AES_KEY);
    }

    public static String encrypt(String data, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKeySpec secretKeySpec = new SecretKeySpec(Base64.getDecoder().decode(key), ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedData) throws Exception {
        return decrypt(encryptedData, UrlUtil.AES_KEY);
    }

    public static String decrypt(String encryptedData, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKeySpec secretKeySpec = new SecretKeySpec(Base64.getDecoder().decode(key), ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes);
    }

    public static void main(String[] args) throws Exception {
        String key = generateKey();
        System.out.println("手机号加密串: " + key);

        String data = "17777777771";
        log.info("原始手机号: " + data);

        String encryptedData = encrypt(data, UrlUtil.AES_KEY);
        log.info("加密手机号: " + encryptedData);

        String decryptedData = decrypt(encryptedData, UrlUtil.AES_KEY);
        log.info("解密手机号: " + decryptedData);
    }
}
