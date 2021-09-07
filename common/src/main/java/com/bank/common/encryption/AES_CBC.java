package com.bank.common.encryption;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES/CBC/PKCS5Padding 对称加密
 *
 */
public class AES_CBC {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String VI = "qwertyuiopasdfgh";
    private static final String KEY = "qwertyuiopasdfgh";

    /**
     * 数据加密
     *
     * @param srcData
     * @return
     */
    public static String encrypt(String srcData) {
        SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), "AES");
        Cipher cipher;
        String encodeBase64String = null;
        try {
            cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(VI.getBytes()));
            byte[] encData = cipher.doFinal(srcData.getBytes());
            encodeBase64String = Base64.encodeBase64String(encData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodeBase64String;
    }

    /**
     * 数据解密
     *
     * @param encDataStr
     * @return
     */
    public static String decrypt(String encDataStr) {
        byte[] encData = Base64.decodeBase64(encDataStr);
        SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), "AES");
        Cipher cipher;
        byte[] decbbdt = null;
        try {
            cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(VI.getBytes()));
            decbbdt = cipher.doFinal(encData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(decbbdt);
    }

    public static void main(String[] args) {
      String aa =  AES_CBC.encrypt("12qw56");
        try {
            System.out.printf(aa);
            String bb = AES_CBC.decrypt(aa);
            System.out.println(bb);
            System.out.printf(AES_CBC.decrypt("xR8wXpKxcJNyt90qHwJi9Q=="));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
