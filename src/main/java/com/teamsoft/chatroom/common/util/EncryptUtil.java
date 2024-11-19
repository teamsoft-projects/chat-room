package com.teamsoft.chatroom.common.util;

import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * 加密/解密工具
 * @author alex
 * @version 2020/6/19
 */
public class EncryptUtil {
	private static final String KEY_ALGORITHM = "AES";
	private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";//默认的加密算法
	private static final byte[] KEY = "zhgerXHBVaaKm8xy".getBytes(StandardCharsets.UTF_8);
	private static String AUTH = "二哈不是哈士奇";
	private static boolean isStopAuth = true;

	/**
	 * 加密
	 */
	public static String encrypt(String content) {
		try {
			SecretKeySpec key = new SecretKeySpec(Arrays.copyOf(KEY, 16), KEY_ALGORITHM);
			Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
			byte[] byteContent = content.getBytes(StandardCharsets.UTF_8);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] byteAes = cipher.doFinal(byteContent);
			// 将加密后的数据转换为字符串
			return Base64Utils.encodeToString(byteAes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 如果有错就返加null
		return null;
	}

	/**
	 * 解密
	 */
	public static String decrypt(String content) {
		try {
			SecretKeySpec key = new SecretKeySpec(Arrays.copyOf(KEY, 16), KEY_ALGORITHM);
			Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
			byte[] byteContent = Base64Utils.decodeFromString(content);
			cipher.init(Cipher.DECRYPT_MODE, key);
			// 解密
			byte[] byteDecode = cipher.doFinal(byteContent);
			return new String(byteDecode, StandardCharsets.UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
		}

		//如果有错就返加null
		return null;
	}

	public static boolean verifyAuth(String auth) {
		return isStopAuth || AUTH.equals(auth);
	}

	public static void updateAuth(String newAuth) {
		AUTH = newAuth;
	}

	public static String getAuth() {
		return AUTH;
	}

	public static void stopAuth() {
		isStopAuth = true;
	}

	public static boolean isStopAuth() {
		return isStopAuth;
	}
}