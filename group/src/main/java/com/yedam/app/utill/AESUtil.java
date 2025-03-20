package com.yedam.app.utill;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {
	private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static byte[] SECRET_KEY;
    private static byte[] IV;

    public static void setKeys(String secretKeyBase64, String ivBase64) {
        if (SECRET_KEY == null && IV == null) {
            SECRET_KEY = Base64.getDecoder().decode(secretKeyBase64);
            IV = Base64.getDecoder().decode(ivBase64);
            System.out.println("🔹 AES Keys Set Successfully!");
            System.out.println("SECRET_KEY Length: " + SECRET_KEY.length);
            System.out.println("IV Length: " + IV.length);
        }
    }

    public static String encrypt(String value) throws Exception {
        if (SECRET_KEY == null || IV == null) {
            throw new IllegalStateException("AES Keys are not initialized. Call setKeys() first.");
        }

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(SECRET_KEY, ALGORITHM), new IvParameterSpec(IV));
        return Base64.getEncoder().encodeToString(cipher.doFinal(value.getBytes(StandardCharsets.UTF_8)));
    }

    public static String decrypt(String encrypted) throws Exception {
        if (SECRET_KEY == null || IV == null) {
            throw new IllegalStateException("AES Keys are not initialized. Call setKeys() first.");
        }

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(SECRET_KEY, ALGORITHM), new IvParameterSpec(IV));
        return new String(cipher.doFinal(Base64.getDecoder().decode(encrypted)), StandardCharsets.UTF_8);
    }
}
