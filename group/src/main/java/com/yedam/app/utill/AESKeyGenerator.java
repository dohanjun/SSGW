package com.yedam.app.utill;

import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class AESKeyGenerator {
	public static void main(String[] args) throws Exception {
        // 1. 32바이트(256비트) AES 키 생성
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256, new SecureRandom());
        SecretKey secretKey = keyGen.generateKey();
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());

        // 2. 16바이트(128비트) IV 생성
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        String encodedIv = Base64.getEncoder().encodeToString(iv);

        // 출력 (환경 변수에 저장 필요)
        System.out.println("AES Key: " + encodedKey);
        System.out.println("IV: " + encodedIv);
    }
}
