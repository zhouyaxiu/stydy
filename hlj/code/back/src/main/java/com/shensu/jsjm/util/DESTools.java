package com.shensu.jsjm.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

public class DESTools {
	private static String KEY = "B573C576D870082B";
	private final static String DES = "DES";
	
	public static void getInstance(String key) {
		KEY = key;
	}

	/**
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] byteMing, byte[] key) throws Exception {
		SecureRandom sr = new SecureRandom();
		DESKeySpec dks = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance(DES);
		cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
		return cipher.doFinal(byteMing);
	}

	/**
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] bMi, byte[] key) throws Exception {
		SecureRandom sr = new SecureRandom();
		DESKeySpec dks = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance(DES);
		cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
		return cipher.doFinal(bMi);
	}

	/**
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] bMing) throws Exception {
		return encrypt(bMing, KEY.getBytes());
	}

	/**
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] bMi) throws Exception {
		return decrypt(bMi, KEY.getBytes());
	}

	/**
	 * @throws Exception
	 */
	public static String encrypt2HexStr(byte[] byteMing) throws Exception {
		try {
			return byte2hex(encrypt(byteMing, KEY.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @throws Exception
	 */
	public static String decryptHexStr(byte[] byteMi) {
		try {
			return new String(decrypt(hex2byte(byteMi), KEY.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @throws Exception
	 */
	public static String encrypt2HexStr(String strMing) throws Exception {
		return encrypt2HexStr(strMing.getBytes());
	}

	/**
	 * @throws Exception
	 */
	public static String decryptHexStr(String strMi) throws Exception {
		return decryptHexStr(strMi.getBytes());
	}

	/**
	 * @throws Exception
	 */
	public static String encrypt2HexStr(String sMing, String key) {
		try {
			return byte2hex(encrypt(sMing.getBytes(), key.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @throws Exception
	 */
	public final static String decryptHexStr(String sMi, String key) {
		try {
			return new String(decrypt(hex2byte(sMi.getBytes()), key.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;

		}
		return hs.toUpperCase();
	}

	public static byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0)
			throw new IllegalArgumentException("���Ȳ���ż��");
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}

	/**
	 * @throws Exception
	 */
	public static String encrypt2Base64(byte[] byteMing) throws Exception {
		try {
			BASE64Encoder base64encoder = new BASE64Encoder();
			return base64encoder.encode(encrypt(byteMing, KEY.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @throws Exception
	 */
	public static String decryptBase64(byte[] byteMi) {
		try {
			String strMi = new String(byteMi);
			BASE64Decoder base64decoder = new BASE64Decoder();
			byte[] bytesMi = base64decoder.decodeBuffer(strMi);
			String strMing = new String(decrypt(bytesMi,KEY.getBytes()));
			return strMing;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String args[]) {
		String sMing = "shenhe";
		String key = "B573C576D870082B";
		com.shensu.jsjm.util.DESTools.getInstance(key);
		String sMi = null;
		String strMing = null;
		System.out.println("ԭ�� = "+sMing+" & key = "+key);
		try {
			sMi = com.shensu.jsjm.util.DESTools.encrypt2Base64(sMing.getBytes());
			System.out.println("���� = "+sMi);
			strMing = com.shensu.jsjm.util.DESTools.decryptBase64(sMi.getBytes("utf-8"));
			System.out.println("���� = "+strMing);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
