package com.clever.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @Author xixi
 * @Date 2023-12-20 11:38
 **/
public class DesUtil {

    public static final String PASSWORD = "PASSWORD";
    private final static Logger log = LoggerFactory.getLogger(DesUtil.class);
    private static final String ENCRYPTION_METHOD = "DES";

    /**
     * 二次加密
     *
     * @param content  内容
     * @param password 密匙
     * @return 密文
     */
    public static String safeEncrypt(String content, String password) {
        String encrypt = encrypt(content, password);
        if (encrypt == null) {
            return null;
        }
        return encrypt(encrypt, DesUtil.PASSWORD);
    }

    /**
     * 二次加密后解密
     *
     * @param ciphertext 密文
     * @param password   密匙
     * @return 明文
     */
    public static String safeDecrypt(String ciphertext, String password) {
        String plaintext = decrypt(ciphertext, DesUtil.PASSWORD);
        if (plaintext == null) {
            return null;
        }
        return decrypt(plaintext, password);
    }

    /**
     * 加密
     *
     * @param content  加密的内容
     * @param password 密匙
     * @return 加密字符串
     */
    public static String encrypt(String content, String password) {
        try {
            Cipher cipher = Cipher.getInstance(ENCRYPTION_METHOD);
            SecretKeySpec sks = new SecretKeySpec(password.getBytes(), ENCRYPTION_METHOD);
            cipher.init(Cipher.ENCRYPT_MODE, sks);
            byte[] bytes = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
            return bytesToHexString(bytes);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            log.warn("DES加密失败", e);
        }
        return null;
    }

    /**
     * 解密字符串
     *
     * @param ciphertext 密文
     * @param password   密匙
     * @return 解密后字符串
     */
    public static String decrypt(String ciphertext, String password) {
        try {
            Cipher cipher = Cipher.getInstance(ENCRYPTION_METHOD);
            SecretKeySpec sks = new SecretKeySpec(password.getBytes(StandardCharsets.UTF_8), ENCRYPTION_METHOD);
            cipher.init(Cipher.DECRYPT_MODE, sks);
            byte[] inputBytes = hexStringToBytes(ciphertext);
            byte[] bytes = cipher.doFinal(inputBytes);
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            log.warn("DES解密失败", e);
        }
        return null;
    }

    /**
     * 字符串转字节数组
     *
     * @param hexString 16进制字符串
     * @return 字节数组
     */
    private static byte[] hexStringToBytes(String hexString) {
        int size = 2;
        if (hexString.length() % size != 0) {
            throw new IllegalArgumentException("hexString length not valid");
        }
        int length = hexString.length() / size;
        byte[] resultBytes = new byte[length];
        for (int index = 0; index < length; index++) {
            String result = hexString.substring(index * size, index * size + 2);
            resultBytes[index] = Integer.valueOf(Integer.parseInt(result, 16)).byteValue();
        }
        return resultBytes;
    }

    /**
     * 字节转16进制字符串
     *
     * @param sources 字节数组
     * @return string
     */
    private static String bytesToHexString(byte[] sources) {
        if (sources == null) {
            return null;
        }
        StringBuilder stringBuffer = new StringBuilder();
        for (byte source : sources) {
            String result = Integer.toHexString(source & 0xff);
            if (result.length() < 2) {
                result = "0" + result;
            }
            stringBuffer.append(result);
        }
        return stringBuffer.toString();
    }
}
