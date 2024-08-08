package com.shensu.jsjm.util;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

@SuppressWarnings("restriction")
public class AESUtil {
    private final static Logger log = LoggerFactory.getLogger(AESUtil.class);
    // aesKey
    private static final String sKey = "lWkfe$GD#GS3f9f4";
    // 算法/模式/补码方式
    private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    @SuppressWarnings({"unused"})
    public static String encrypt(String sSrc) {
        String originalString = "";
        try {
            if (sKey == null) {
                log.info("Key为空null");
                return null;
            }
            // 判断key是否为16位
            if (sKey.length() != 16) {
                log.info("Key的长度不是16位");
                return null;
            }
//		byte[] raw = sKey.getBytes("ASCII");
            byte[] raw = sKey.getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

            byte[] srawt = sSrc.getBytes("UTF-8");
            int len = srawt.length;
            // 计算补0后的长度
            while (len % 16 != 0)
                len++;
            byte[] sraw = new byte[len];
            // 在最后补0
            for (int i = 0; i < len; ++i) {
                if (i < srawt.length) {
                    sraw[i] = srawt[i];
                } else {
                    sraw[i] = 0;
                }
            }
            byte[] encrypted = cipher.doFinal(sraw);
            // 此处使用BASE64做转码功能，同时能起到2次加密的作用
            originalString = new BASE64Encoder().encode(encrypted);
            originalString = originalString.replaceAll("\n", "");
            originalString = originalString.replaceAll("\r", "");
            return originalString;
        } catch (Exception e) {
            log.info(e.toString());
            return originalString;
        }
    }

    /**
     * 解密
     *
     * @param sSrc
     * @return
     * @throws Exception
     */
    @SuppressWarnings({"unused"})
    public static String decrypt(String sSrc) {
        String originalString = "";
        try {
            if (sKey == null) {
                log.info("Key为空null");
                return null;
            }
            if (sKey.length() != 16) {
                log.info("Key的长度不是16位");
                return null;
            }
//			byte[] raw = sKey.getBytes("ASCII");
            byte[] raw = sKey.getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            // 先用Base64解密
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);
            byte[] original = cipher.doFinal(encrypted1);
            originalString = new String(original, "UTF-8");
            return originalString.trim();
        } catch (Exception ex) {
            log.info(ex.toString());
            return null;
        }
    }

    /**
     * RSA公钥加密
     *
     * @param str       加密字符串
     * @param publicKey 公钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String encryptRSA(String str, String publicKey) throws Exception {
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return Base64.encodeBase64String(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
    }
}
