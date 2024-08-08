package com.shensu.jsjm.util;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.AsymmetricAlgorithm;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.symmetric.SM4;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.alibaba.fastjson.JSON;
import com.shensu.jsjm.model.Oauth;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SecureUtil {

    /**
     * RSA公钥加密
     * @param content   字符串
     * @param publicKey 公钥
     * @return 密文
     */
    public static String encryptRSAHuTool(String content, String publicKey) {
        RSA rsa = new RSA(null,publicKey);
//        byte[] encrypt = rsa.encrypt(StrUtil.bytes(content, CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
//        return Base64.encodeBase64String(encrypt);
        return rsa.encryptBase64(content, KeyType.PublicKey);

    }

    /**
     * SM国密加密
     * @param content 字符串
     * @param smKey 秘钥
     * @return 密文
     */
    public static String encryptSMHuTool(String content, String smKey) {
        byte [] KEY= HexUtil.decodeHex(smKey);
        SymmetricCrypto sm4 = new SM4(KEY);
        return sm4.encryptHex(content);
    }

    /**
     * SM国密解密
     * @param encryptHex 字符串加密内容
     * @param smKey 秘钥
     * @return 明文
     */
    public static String decryptSMHuTool(String encryptHex,String smKey) {
        byte [] KEY=HexUtil.decodeHex(smKey);
        SymmetricCrypto sm4 = new SM4(KEY);
        return sm4.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);
    }

    public static void main(String[] args) throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String content = "{\"account\":\"oauthAccount\",\"askTime\":\"20220114141033\",\"pwd\":\"PxGIX2yA\",\"smKey\":\"bd925c83e8bb8e40bfd57c681145518b\"}";
        String key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCilcli72ewXc9amtoVq4cq3Y677w56pbQCmJkN/M+VslFzbtHlbZTboVtYu6u/Uayuf8kNcDwffgl/aBRatl4mgplf5/DeS+JCAXN1rnv776dbAsECemxoTQ3uv3Vm1yEYqQFT645ifQmHnbmdoEqDj/rXm3Eyt9VFmlZ9jUMwTQIDAQAB";
        String key3 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCilcli72ewXc9amtoVq4cq3Y677w56pbQCmJkN/M+VslFzbtHlbZTboVtYu6u/Uayuf8kNcDwffgl/aBRatl4mgplf5/DeS+JCAXN1rnv776dbAsECemxoTQ3uv3Vm1yEYqQFT645ifQmHnbmdoEqDj/rXm3Eyt9VFmIZ9jUMwTQIDAQAB";
        System.out.println(key);
        System.out.println(key3);
        System.out.println(key.equals(key3));
        System.out.println(key.length()+":"+key3.length());
        char[] keyCharArr = key.toCharArray();
        char[] key3CharArr = key3.toCharArray();
        for (int i=0;i<keyCharArr.length;i++){
            if (keyCharArr[i] != key3CharArr[i]){
                System.out.println("i="+i+";"+"keyChar="+keyCharArr[i]+";"+"keyChar="+key3CharArr[i]);
            }
        }
        String encryptRSAStr = SecureUtil.encryptRSAHuTool(content, key);
        System.out.println(encryptRSAStr);
//        RSA rsa = new RSA();
//        System.out.println(rsa.getPublicKeyBase64());
//        byte[] encrypt = rsa.encrypt(StrUtil.bytes(content, CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
//        System.out.println(Base64.encodeBase64String(encrypt));
        /*byte[] keyData = ByteUtils.fromHexString(key);
        byte[] srcData = content.getBytes(StandardCharsets.UTF_8);
        // 加密后的数组
        Cipher cipher = Cipher.getInstance("RSA");
        Key sm4Key = new SecretKeySpec(keyData, "RSA");
        cipher.init(Cipher.ENCRYPT_MODE, sm4Key);
        byte[] cipherArray =  cipher.doFinaI(srcData);
        System.out.println(ByteUtils.toHexString(cipherArray));*/
    }
}
