package com.shensu.jsjm.util;

/**
 * Created by hezn on 15-9-1.
 * DES加密算法
 */
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;

public class DESPlus {
    private static String strDefaultKey = "ssnjjszx";
    private Cipher encryptCipher = null;
    private Cipher decryptCipher = null;

    public static String getStrDefaultKey() {
        return strDefaultKey;
    }

    public static void setStrDefaultKey(String strDefaultKey) {
        DESPlus.strDefaultKey = strDefaultKey;
    }

    public static String byteArr2HexStr(byte[] arrB) throws Exception {
        int iLen = arrB.length;
        // 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = arrB[i];
            // 把负数转换为正数
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            // 小于0F的数需要在前面补0
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

    public static byte[] hexStr2ByteArr(String strIn) throws Exception {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;
        // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }



    public DESPlus() throws Exception {
        this(strDefaultKey);
    }

    public DESPlus(String strKey) throws Exception {
        Security.addProvider(new com.sun.crypto.provider.SunJCE());
        Key key = getKey(strKey.getBytes());
        encryptCipher = Cipher.getInstance("DES");
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);
        decryptCipher = Cipher.getInstance("DES");
        decryptCipher.init(Cipher.DECRYPT_MODE, key);
    }

    public byte[] encrypt(byte[] arrB) throws Exception {
        return encryptCipher.doFinal(arrB);
    }

    public String encrypt(String strIn) throws Exception {
        return byteArr2HexStr(encrypt( strIn.getBytes()));
    }

    public byte[] decrypt(byte[] arrB) throws Exception {
        return decryptCipher.doFinal(arrB);
    }

    public String decrypt(String strIn) throws Exception {
       //return new String(decrypt(hexStr2ByteArr(strIn)));
        return new String(decrypt(hexStr2ByteArr(strIn)),"UTF-8");
    }

    private Key getKey(byte[] arrBTmp) throws Exception {
        // 创建一个空的8位字节数组（默认值为0）
        byte[] arrB = new byte[8];
        // 将原始字节数组转换为8位
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }
        // 生成密钥
        Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");
        return key;
    }

    public  static String getEncryptByKey(String key,String encryptStr){
        if(key==null||key.length()==0||encryptStr==null||encryptStr.length()==0){
            return "";
        }
        try {

           // String test = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><US><AreaCode>460000000000</AreaCode><OrganName>海南省疾控中心</OrganName><LoginName>admin</LoginName><OrganCode>460000000000</OrganCode><AreaName>海南省</AreaName><UserName>超级管理员</UserName></US>";
            //DESPlus des = new DESPlus();//默认密钥
            DESPlus des = new DESPlus(key);//自定义密钥
            //System.out.println("加密前的字符："+encryptStr);

            String retString=des.encrypt(encryptStr);
            //System.out.println("加密后的字符："+retString);
            //String retString=des.decrypt(des.encrypt(encryptStr));
            //System.out.println("解密后的字符1111："+des.decrypt(retString));
            return retString;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return "";
        }
    }


    public  static String getDencryptByKey(String key,String encryptStr){
        if(key==null||key.length()==0||encryptStr==null||encryptStr.length()==0){
            return "";
        }
        try {


            DESPlus des = new DESPlus(key);//自定义密钥
           // System.out.println("解密前的字符2222222："+encryptStr);
            String retString=des.decrypt(encryptStr);
            //System.out.println("解密后的字符3333："+retString);


            return retString;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return "";
        }
    }

    public static byte[] decrypt(byte[] src, String password) throws Exception {
        SecureRandom random = new SecureRandom();
        DESKeySpec desKey = new DESKeySpec(password.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(desKey);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, securekey);
        return cipher.doFinal(src);
    }


    public static void main(String[] args) {
        // TODO Auto-generated method stub
        try {
           //String test = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><US><AreaCode>460000000000</AreaCode><OrganName>海南省疾控中心</OrganName><LoginName>admin</LoginName><OrganCode>460000000000</OrganCode><AreaName>海南省</AreaName><UserName>超级管理员</UserName></US>";
           // DESPlus des = new DESPlus();//默认密钥
           // DESPlus des = new DESPlus("be2d3e803d5046f98b9f4c0798e915ab");//自定义密钥
           //System.out.println("加密前的字符："+test);
           // System.out.println("加密后的字符："+des.encrypt(test));
           // System.out.println("解密后的字符："+des.decrypt(des.encrypt(test)));

//            String str = getDencryptByKey("6d9316f3de854c478eccec22b2be1b0a","3e8d7e82f9b986599c426d7e0c12f313018e77fe0fdec5dde0dba47abaf5b1e69799ccadcb2d638ad22a8a02a767dc7d6a552da97041171d52196e61136a8a857afa2b53b0d9fa0a98efc127979d9084f56d2a284e5fb833b890654b61e318427e985bd99c796068cfb0e7eba24575310bafa708198173d0ba24e0f627de12247e0017bafeed66dd0f7ce6493e51c9c9fac0150dca969f42fb9c090539456034");
//            byte[] bb= decrypt(hexStr2ByteArr("3e8d7e82f9b986599c426d7e0c12f313018e77fe0fdec5dde0dba47abaf5b1e69799ccadcb2d638ad22a8a02a767dc7d6a552da97041171d52196e61136a8a857afa2b53b0d9fa0a98efc127979d9084f56d2a284e5fb833b890654b61e318427e985bd99c796068cfb0e7eba24575310bafa708198173d0ba24e0f627de12247e0017bafeed66dd0f7ce6493e51c9c9fac0150dca969f42fb9c090539456034"),"6d9316f3de854c478eccec22b2be1b0a");
//            System.out.println(str);

            String str = getDencryptByKey("f625e17635184e30a1dbbbda68b6b6b2","c4f4bd071971922f3ba097535a2b62ab65ba3f5eb696405153912596f7a67d13946823076e8a2f21f59cea642ba4f4e35be3521139b48d475de9fc0d296f36f0bc680514470715796cfa433c2d4b49df7dc84263c7b605e44dd336d871c27d3214a16b0997c739e08b08f36762d3fd15febc371574a6d010fc71a41f76133081bc2e2a60f697aaee1b9948b502a1d523d51839e813ddcda8e3d1ca0ee4690c3a");
            System.out.println("============"+str);

//              byte[] bb= decrypt(hexStr2ByteArr("c4f4bd071971922f3ba097535a2b62ab65ba3f5eb696405153912596f7a67d13946823076e8a2f21f59cea642ba4f4e35be3521139b48d475de9fc0d296f36f0bc680514470715796cfa433c2d4b49df7dc84263c7b605e44dd336d871c27d3214a16b0997c739e08b08f36762d3fd15febc371574a6d010fc71a41f76133081bc2e2a60f697aaee1b9948b502a1d523d51839e813ddcda8e3d1ca0ee4690c3a"),"f625e17635184e30a1dbbbda68b6b6b2");
//              System.out.println(new String(bb,"UTF-8"));

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

}



