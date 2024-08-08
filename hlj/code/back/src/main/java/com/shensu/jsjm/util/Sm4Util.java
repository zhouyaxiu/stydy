package com.shensu.jsjm.util;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.alibaba.fastjson.JSONObject;
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
public class Sm4Util {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private static final Logger log = LoggerFactory.getLogger(Sm4Util.class);

    private static final String sKey = "6d476239336e624a6966383135346441";
    private static final String JMBkey="sq@dixe!wdlfoy#m";
    private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";//�㷨/ģʽ/���뷽ʽ
    private static final String ENCODING = "UTF-8";
    public static final String ALGORITHM_NAME = "SM4";

    public static final String ALGORITHM_NAME_ECB_PADDING = "SM4/ECB/PKCS5Padding";

    public static final int DEFAULT_KEY_SIZE = 128;

    public static String JMBSM4(String content) {

 //       String key="sq@dixe!wdlfoy#m";

 //       String content = "{\"code\":0,\"data\":[{\"stationCode\":\"6401210104\",\"score\":\"152\",\"num\":\"34\"}],\"message\":\"��ȡ�ɹ�\",\"success\":true}";

        SymmetricCrypto sm4 = SmUtil.sm4(JMBkey.getBytes());


 //       String encryptHex = sm4.encryptHex(content);

        // ����
        String decryptStr = sm4.decryptStr(content, CharsetUtil.CHARSET_UTF_8);

        return decryptStr;
    }


    private static Cipher generateEcbCipher(String algorithmName, int mode, byte[] key) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithmName, BouncyCastleProvider.PROVIDER_NAME);
        Key sm4Key = new SecretKeySpec(key, ALGORITHM_NAME);
        cipher.init(mode, sm4Key);
        return cipher;
    }

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

    public static String encryptEcb(String hexKey, String paramStr) throws Exception {
        String cipherText = "";
        // 16�����ַ���--&gt;byte[]
        byte[] keyData = ByteUtils.fromHexString(hexKey);
        // String--&gt;byte[]
        byte[] srcData = paramStr.getBytes(ENCODING);
        // ���ܺ������
        byte[] cipherArray = encrypt_Ecb_Padding(keyData, srcData);

        // byte[]--&gt;hexString
        cipherText = ByteUtils.toHexString(cipherArray);
        return cipherText;
    }


    public static byte[] encrypt_Ecb_Padding(byte[] key, byte[] data) throws Exception {
        Cipher cipher = generateEcbCipher(ALGORITHM_NAME_ECB_PADDING, Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    public static String decryptEcb(String cipherText) {
        String decryptStr = "";
        try {
            byte[] keyData = ByteUtils.fromHexString(sKey);
            byte[] cipherData = ByteUtils.fromHexString(cipherText);
            byte[] srcData = decrypt_Ecb_Padding(keyData, cipherData);
            decryptStr = new String(srcData, ENCODING);
        } catch (Exception e) {
            log.info(e.toString());
        }

        return decryptStr;
    }

    public static String decryptEcb(String key , String cipherText) {
        String decryptStr = "";
        try {
            byte[] keyData = ByteUtils.fromHexString(key);
            byte[] cipherData = ByteUtils.fromHexString(cipherText);
            byte[] srcData = decrypt_Ecb_Padding(keyData, cipherData);
            decryptStr = new String(srcData, ENCODING);
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.toString());
        }

        return decryptStr;
    }


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

//    public static String decryptForJKWeChat(String sSrc) {//����
//
//        if (jksKey == null) {
//            log.info("KeyΪ��null");
//            return null;
//        }
//        if (jksKey.length() != 16) {
//            log.info("Key�ĳ��Ȳ���16λ");
//            return null;
//        }
//        return decrypt(jksKey, sSrc);
//    }

//    public static Map<String, Object> getParamsForGetForWeChat(String params) {
//        Map<String, Object> info = new HashMap<String, Object>();
//        if (StringUtils.isNotBlank(params)) {
//            String strValue = Sm4Util.decryptForJKWeChat(params);
//            info = JSONObject.parseObject(strValue, Map.class);
//            if (!info.isEmpty()) {
//                for (Map.Entry<String, Object> entry : info.entrySet()) {
//                    String mapKey = entry.getKey();
//                    Object mapValue = entry.getValue();
//                    if (mapValue != null &&
//                            (mapValue.toString().contains("%") || mapValue.toString().contains("_") || mapValue.toString().contains("\\"))) {
////                        String str = StringUtils.replace(mapValue.toString(), "_", "\\_");
////                        str = StringUtils.replace(str, "\\", "\\\\");
//                        String[] strs = mapValue.toString().split("");
//                        String str = "";
//                        for (String s : strs) {
//                            if (s.equals("_")) {
//                                str += "\\" + s;
//                            } else if (s.equals("\\")) {
//                                str += "\\" + s;
//                            } else if (s.equals("%")) {
//                                str += "\\" + s;
//                            } else {
//                                str += s;
//                            }
//                            info.put(mapKey, str.toString());
//                        }
//                    }
//                }
//
//            }
//        }
//        return info;
//    }


    public static String encrypt(String sKey, String sSrc) throws Exception {
        byte[] raw = sKey.getBytes("ASCII");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] srawt = sSrc.getBytes("UTF-8");
        int len = srawt.length;
        while (len % 16 != 0) len++;
        byte[] sraw = new byte[len];
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
        //String originalString=new BASE64Encoder().encode(encrypted);
        originalString = originalString.replaceAll("\n", "");
        originalString = originalString.replaceAll("\r", "");
        return originalString;
    }

//    public static String encryptDB(String sSrc) throws Exception {
//        if (DBKey == null) {
//            log.info("KeyΪ��null");
//            return null;
//        }
//        if (DBKey.length() != 16) {//�ж�key�Ƿ�Ϊ16λ
//            log.info("Key�ĳ��Ȳ���16λ");
//            return null;
//        }
//        return encrypt(DBKey, sSrc);
//    }


    public static String decrypt(String sKey, String sSrc) {//����
        try {

            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);

            /*byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);//����Base64����  */
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

//    public static String decryptdb(String sSrc) {//����
//        if (DBKey == null) {
//            log.info("KeyΪ��null");
//            return null;
//        }
//        if (DBKey.length() != 16) {
//            log.info("Key�ĳ��Ȳ���16λ");
//            return null;
//        }
//        return decrypt(DBKey, sSrc);
//    }

//    public static String encryptForJKWeChat(String sSrc) throws Exception {
//        if (jksKey == null) {
//            log.info("KeyΪ��null");
//            return null;
//        }
//        if (jksKey.length() != 16) {//�ж�key�Ƿ�Ϊ16λ
//            log.info("Key�ĳ��Ȳ���16λ");
//            return null;
//        }
//        return encrypt(jksKey, sSrc);//"mGb93nbJif8154dA"
//    }


    public static boolean verifyEcb(String cipherText, String paramStr) throws Exception {
        boolean flag = false;
        byte[] keyData = ByteUtils.fromHexString(sKey);
        byte[] cipherData = ByteUtils.fromHexString(cipherText);
        byte[] decryptData = decrypt_Ecb_Padding(keyData, cipherData);
        byte[] srcData = paramStr.getBytes(ENCODING);
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

//    public static Map<String, Object> getParamsForWeChat(String params) {
//        Map<String, Object> info = new HashMap<>();
//        if (StringUtils.isNotBlank(params)) {
//            String strValue = AesUtil.decryptForJKWeChat(params);
//            info = JSONObject.parseObject(strValue, Map.class);
//        }
//        return info;
//    }

    public static void main(String[] args) throws Exception {
//        int i = 1;
//        int j=3;
//
//        System.out.println("============"+ (double) i/j);
//        System.out.println("============"+ (i == 0));
//
//
//        i++;
//        i++;
//        System.out.println("============"+i);


//        try {
//            // String src = "{\"username\":\"ADMIN2\",\"password\":\"3649a2dcd8886e19e434e0dadd1a1503\",\"validcode\":\"45XY\"}";
//            //String src="{\"token\":\"e512ea8d10a21362ad487ed1634a50e5\",\"loginname\":\"ADMIN2\",\"areacode\":\"644200000000\"}";
//           // String src="{\"areacode\":\"6400000000\",\"begintime\":\"2015-01\",\"endtime\":\"2015-01\",\"tjtype\":\"0\"}";
//           // String src="{\"areacode\":\"640100000000\",\"begintime\":\"2008-05\",\"endtime\":\"2008-05\",\"tjtype\":\"0\",\"regtypes\":\"��ס;����������;�е�������;��ʡ������;ʡ��������;��������;����;��ʱ\",\"token\":\"dcd78078839415bfcae9b3e641eaa634\",\"loginname\":\"ADMIN2\"}";
//          //  String src="{\"areacode\":\"640100000000\",\"begintime\":\"2008-05\",\"endtime\":\"2008-05\",\"tjtype\":\"0\",\"regtypes\":\"��ס;����������;�е�������;��ʡ������;ʡ��������;��������;����;��ʱ\",\"token\":\"56ab8607d47b7ced1076ef1e5c2c56c9\",\"loginname\":\"ADMIN2\",\"skipvalue\":\"1\"}";
//          //  String src="{\"token\":\"8a15de436ac376130cb54c14ac8f92b2\",\"loginname\":\"ADMIN2\",\"type\":\"RESITYPE\"}";
//           String src="{\"selectAreaCode\":\"640100000000\",\"selectAreaName\":\"����\",\"begintime\":\"2008-05\",\"endtime\":\"2008-05\",\"tjtype\":\"0\",\"resiType\":\"452\",\"token\":\"8f1e09b3d471022f9c5d0fc8e7169291\",\"loginname\":\"ADMIN2\",\"selectType\":\"2\",\"selectAreaId\":\"1\",\"selectAreaLevel\":\"0\"}";
//            String data = Sm4Util.encryptEcb("6d476239336e624a6966383135346441",src);
//            System.out.println(data);
//
//            Map<String, Object> info = Sm4Util.getParams("82c3b66322840cdfe03e52b19a1e0efdaf976d5c3c9ddeed9403a7cc9c044932249c2b5bfb1d1239d5df5eec584c4dd8ac11879bf94be18b3e11de516d8d3d57446e8c6f6ddfa013e145956fcc377c9b254af2e7dd4a5302c832e481baa8f9d2fbc67eb03bbb3452b53dc3891023a3a4bbae716baa21b7f9e1d06a92c9f4ba9c60260dbe20e44fb318230664efaf9430bdc3156055244d9daf8e85ddf99900cb06c35619a9ec3cc9d9dfc58754ce7e92797fa30ef6765b7a857ee136d5db90d695d247d11db06027cc2436b91608298415efb24945e53195922ac45cd70a01cade87940f02a836668baffc1f108837b553a37150a65b21490531222de33a9ac30d905cad003bcde78e2d6af90170094aeab65710bf0f8cd6a127839742e11601d0c59d332736a4b72c2b05a0688126488a8a46757bf4693f32b2358e7408f32fec55955398cc93478ad2008bf1b529d6d2f6f51efe35738d71493dffaa806e4c");
//            System.out.println(info);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        String key = "8841255d04644c848166ad2490036b53";
       String str = "2a401b00a881446c77f463a5afaccfc489ff1cc8638b43d82492ed9f057f2acae8e7bd00a7d56153f3facd826c5bd977ccd84e4db3a9e603adbedb2c23ce2e940f8c37916491827fa0da95d1c9b1ea45bc29f42aea5cc0b9f85243b2c42f1a3343f583a911615b9a42fd63b56f15b6b589da8ed3db525c32fc94bbd0ea3b5932f63075d321638b6367c58e5d25083f6a766c87c53372b90a83509e2b966dfc0355a13adb258edbce1b3b8b94184680d305884c48ce629bd3b885e95acac0984d55febf455db46672a1714f8dfd629d1f";
        System.out.println("======="+ Sm4Util.decryptEcb(key, str));

//        System.out.println(Sm4Util.decryptEcb("397c0eca92f4e0caf7555ae53e174338", "636CD40E8E65238E874EFC4BD8CBF6DBA2B556FBD428FC5E93D3EC3EE1C11E034AB0321513AFF3C397EC6B69DC86CA307" +
//                "2476AEE9A470C991C9644D39087A7547BA0745A41BB3F93E961DC931DC3EC6608A583EE460F97E40917814CEAF8D07602" +
//                "FAB7B118AFC95E7FE12FBEE5D7AF2EED1F7AE6402184A31AC5708DD5467958"));

    }
}
