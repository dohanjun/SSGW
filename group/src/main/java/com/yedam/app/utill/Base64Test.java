package com.yedam.app.utill;

import java.util.Base64;

public class Base64Test {
	public static void main(String[] args) {
        // application.properties에 설정된 AES 키와 IV 값 사용
        String secretKeyBase64 = "xrtqktFDUfZGVwHDfyY1kBDsaqJKbYbt7Hdex9U3a1Y=";
        String ivBase64 = "9zCLopl+iRLrC3VQdwYC1g==";

        // Base64 디코딩
        byte[] secretKey = Base64.getDecoder().decode(secretKeyBase64);
        byte[] iv = Base64.getDecoder().decode(ivBase64);

        // 결과 출력
        System.out.println("Decoded Secret Key Length: " + secretKey.length);
        System.out.println("Decoded IV Length: " + iv.length);
        System.out.println("Decoded Secret Key (Hex): " + bytesToHex(secretKey));
        System.out.println("Decoded IV (Hex): " + bytesToHex(iv));
    }

    // 바이트 배열을 16진수 문자열로 변환하는 함수
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}
