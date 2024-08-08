package com.shensu.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/* sm4加密算法工具类
 * @explain sm4加密、解密与加密结果验证
 *          可逆算法
 * @creationTime 2018年7月6日上午11:46:59
 * @version 1.0
 * @since
 */
public class Sm4Util {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * @Fields LOGGER : 日志
     */
    private static final Logger log = LoggerFactory.getLogger(Sm4Util.class);

    private static final String sKey = "6d476239336e624a6966383135346441";
    private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";//算法/模式/补码方式
    private static final String ENCODING = "UTF-8";
    public static final String ALGORITHM_NAME = "SM4";

    // 加密算法/分组加密模式/分组填充方式
    // PKCS5Padding-以8个字节为一组进行分组加密
    // 定义分组加密模式使用：PKCS5Padding
    public static final String ALGORITHM_NAME_ECB_PADDING = "SM4/ECB/PKCS5Padding";
    // 128-32位16进制；256-64位16进制
    public static final int DEFAULT_KEY_SIZE = 128;

    /**
     * 生成ECB暗号
     *
     * @param algorithmName 算法名称
     * @param mode          模式
     * @param key
     * @return
     * @throws Exception
     * @explain ECB模式（电子密码本模式：Electronic codebook）
     */
    private static Cipher generateEcbCipher(String algorithmName, int mode, byte[] key) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithmName, BouncyCastleProvider.PROVIDER_NAME);
        Key sm4Key = new SecretKeySpec(key, ALGORITHM_NAME);
        cipher.init(mode, sm4Key);
        return cipher;
    }

    //方法1======================================
    /* 自动生成密钥
     * @explain
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     */
    public static byte[] generateKey() throws Exception {
        return generateKey(DEFAULT_KEY_SIZE);
    }

    /**
     * @param keySize
     * @return
     * @throws Exception
     * @explain
     */
    public static byte[] generateKey(int keySize) throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM_NAME, BouncyCastleProvider.PROVIDER_NAME);
        kg.init(keySize, new SecureRandom());
        return kg.generateKey().getEncoded();
    }
    //方法1======================================结束

    public static String encryptEcb(String paramStr) throws Exception {
        String cipherText = "";
        // 16进制字符串--&gt;byte[]
        byte[] keyData = ByteUtils.fromHexString(sKey);
        // String--&gt;byte[]
        byte[] srcData = paramStr.getBytes(ENCODING);
        // 加密后的数组
        byte[] cipherArray = encrypt_Ecb_Padding(keyData, srcData);

        // byte[]--&gt;hexString
        cipherText = ByteUtils.toHexString(cipherArray);
        return cipherText;
    }

    //方法二：自己提供16进制的密钥==================================


    /* sm4加密
     *    方法二：自己提供16进制的密钥
     * @explain 加密模式：ECB
     *          密文长度不固定，会随着被加密字符串长度的变化而变化
     * @param hexKey
     *            16进制密钥（忽略大小写）
     * @param paramStr
     *            待加密字符串
     * @return 返回16进制的加密字符串
     * @throws Exception
     */
    public static String encryptEcb(String hexKey, String paramStr) throws Exception {
        String cipherText = "";
        // 16进制字符串--&gt;byte[]
        byte[] keyData = ByteUtils.fromHexString(hexKey);
        // String--&gt;byte[]
        byte[] srcData = paramStr.getBytes(ENCODING);
        // 加密后的数组
        byte[] cipherArray = encrypt_Ecb_Padding(keyData, srcData);

        // byte[]--&gt;hexString
        cipherText = ByteUtils.toHexString(cipherArray);
        return cipherText;
    }

    /**
     * 加密模式之Ecb
     *
     * @param key
     * @param data
     * @return
     * @throws Exception
     * @explain
     */
    public static byte[] encrypt_Ecb_Padding(byte[] key, byte[] data) throws Exception {
        Cipher cipher = generateEcbCipher(ALGORITHM_NAME_ECB_PADDING, Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    //方法二：自己提供16进制的密钥==================================结束


    //SM4解密=================================================

    /* sm4解密
     * @explain 解密模式：采用ECB
     * @param hexKey
     *            16进制密钥
     * @param cipherText
     *            16进制的加密字符串（忽略大小写）
     * @return 解密后的字符串
     * @throws Exception
     */
    public static String decryptEcb(String cipherText) {
        String decryptStr = "";
        try {
            // 用于接收解密后的字符串
            // hexString--&gt;byte[]
            byte[] keyData = ByteUtils.fromHexString(sKey);
            // hexString--&gt;byte[]
            byte[] cipherData = ByteUtils.fromHexString(cipherText);
            // 解密
            byte[] srcData = decrypt_Ecb_Padding(keyData, cipherData);
            // byte[]--&gt;String
            decryptStr = new String(srcData, ENCODING);
        } catch (Exception e) {
            log.info(e.toString());
        }

        return decryptStr;
    }

    /**
     * 解密
     *
     * @param key
     * @param cipherText
     * @return
     * @throws Exception
     * @explain
     */
    public static byte[] decrypt_Ecb_Padding(byte[] key, byte[] cipherText) throws Exception {
        Cipher cipher = generateEcbCipher(ALGORITHM_NAME_ECB_PADDING, Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(cipherText);
    }

    public static Map<String, Object> getParams(String params) {
        Map<String, Object> info = new HashMap<String, Object>();
        try {
            if (StringUtils.isNotBlank(params)) {
                String strValue = Sm4Util.decryptEcb(params);
                info = JSONObject.parseObject(strValue, Map.class);
            }
        } catch (Exception e) {

        }
        return info;
    }

    public static JSONObject getParameters(String params) {
        JSONObject info = null;
        try {
            if (StringUtils.isNotBlank(params)) {
                String strValue = Sm4Util.decryptEcb(params);
                info = JSON.parseObject(strValue);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (info == null) {
                info = new JSONObject();
            }
        }
        return info;
    }

    public static String encryptParams(String params) throws Exception {
        String paramsParams = encryptEcb(params);
        return paramsParams;
    }

    /**
     * 解密、封装入参
     * @param params params
     * @return JSONObject
     */
    public static JSONObject getDecryptParams(String params) {
        JSONObject result = null;
        if (StringUtils.isNotBlank(params)) {
            try {
                String strValue = Sm4Util.decryptEcb(params);
                result = JSONObject.parseObject(strValue);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return result;
    }


    /**
     * 〈简述〉解析 前端参数 params  用于 get请求
     * 〈详细描述〉
     *
     * @param params
     * @return
     * @author gelongyu
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getParamsForGet(String params) {
        Map<String, Object> info = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(params)) {
            String strValue = Sm4Util.decryptEcb(params);
            info = JSONObject.parseObject(strValue, Map.class);
            if (!info.isEmpty()) {
                for (Map.Entry<String, Object> entry : info.entrySet()) {
                    String mapKey = entry.getKey();
                    Object mapValue = entry.getValue();
                    if (mapValue != null &&
                            (mapValue.toString().contains("%") || mapValue.toString().contains("_") || mapValue.toString().contains("\\"))) {
//                        String str = StringUtils.replace(mapValue.toString(), "_", "\\_");
//                        str = StringUtils.replace(str, "\\", "\\\\");
                        String[] strs = mapValue.toString().split("");
                        String str = "";
                        for (String s : strs) {
                            if (s.equals("_")) {
                                str += "\\" + s;
                            } else if (s.equals("\\")) {
                                str += "\\" + s;
                            } else if (s.equals("%")) {
                                str += "\\" + s;
                            } else {
                                str += s;
                            }
                            info.put(mapKey, str.toString());
                        }
                    }
                }

            }
        }

        return info;
    }

    /**
     * key,作为参数传递
     *
     * @param sKey
     * @param sSrc
     * @return
     * @throws Exception
     */
    public static String encrypt(String sKey, String sSrc) throws Exception {

        byte[] raw = sKey.getBytes("ASCII");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        byte[] srawt = sSrc.getBytes("UTF-8");
        int len = srawt.length;
        //计算补0后的长度
        while (len % 16 != 0) len++;
        byte[] sraw = new byte[len];
        //在最后补0
        for (int i = 0; i < len; ++i) {
            if (i < srawt.length) {
                sraw[i] = srawt[i];
            } else {
                sraw[i] = 0;
            }
        }
        byte[] encrypted = cipher.doFinal(sraw);

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < encrypted.length; i++) {
            String hex = Integer.toHexString(encrypted[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        String originalString = sb.toString();

        //此处使用BASE64做转码功能，同时能起到2次加密的作用
        //String originalString=new BASE64Encoder().encode(encrypted);
        originalString = originalString.replaceAll("\n", "");
        originalString = originalString.replaceAll("\r", "");
        return originalString;
    }


    /**
     * key,作为参数传递  解码
     *
     * @param sKey
     * @param sSrc
     * @return
     */
    public static String decrypt(String sKey, String sSrc) {//解密
        try {

            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);

            /*byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);//先用Base64解密  */
            byte[] encrypted1 = new byte[sSrc.length() / 2];
            for (int i = 0; i < sSrc.length() / 2; i++) {
                int high = Integer.parseInt(sSrc.substring(i * 2, i * 2 + 1), 16);
                int low = Integer.parseInt(sSrc.substring(i * 2 + 1, i * 2 + 2), 16);
                encrypted1[i] = (byte) (high * 16 + low);
            }

            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original, "UTF-8");
                return originalString.trim();
            } catch (Exception e) {
                return null;
            }
        } catch (Exception ex) {
            return null;
        }
    }

    /* 校验加密前后的字符串是否为同一数据
     * @explain
     * @param hexKey
     *            16进制密钥（忽略大小写）
     * @param cipherText
     *            16进制加密后的字符串
     * @param paramStr
     *            加密前的字符串
     * @return 是否为同一数据
     * @throws Exception
     */
    public static boolean verifyEcb(String cipherText, String paramStr) throws Exception {
        // 用于接收校验结果
        boolean flag = false;
        // hexString--&gt;byte[]
        byte[] keyData = ByteUtils.fromHexString(sKey);
        // 将16进制字符串转换成数组
        byte[] cipherData = ByteUtils.fromHexString(cipherText);
        // 解密
        byte[] decryptData = decrypt_Ecb_Padding(keyData, cipherData);
        // 将原字符串转换成byte[]
        byte[] srcData = paramStr.getBytes(ENCODING);
        // 判断2个数组是否一致
        flag = Arrays.equals(decryptData, srcData);
        return flag;
    }

    public static String handleCardid(String cardid, Object idtype) {
        if ("01".equals(idtype)) {
            return StringUtils.isNotBlank(cardid) ? cardid.substring(0, 4) + "****" + cardid.substring(cardid.length() - 4, cardid.length()) : null;
        } else {
            if (StringUtils.isNotBlank(cardid)) {
                if (cardid.length() < 4) {
                    return "******";
                } else {
                    return cardid.substring(0, 2) + "******" + cardid.substring(cardid.length() - 2, cardid.length());
                }
            } else {
                return null;
            }

        }


    }
}
