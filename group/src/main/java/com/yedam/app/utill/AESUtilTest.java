package com.yedam.app.utill;

public class AESUtilTest {
	public static void main(String[] args) {
		try {
            // Base64 인코딩된 AES 키 & IV (application.properties에서 가져온 값)
            String secretKeyBase64 = "xrtqktFDUfZGVwHDfyY1kBDsaqJKbYbt7Hdex9U3a1Y=";
            String ivBase64 = "9zCLopl+iRLrC3VQdwYC1g==";

            // 🔹 AES 키 설정 (AESInitializer가 실행되지 않는 경우 필요)
            AESUtil.setKeys(secretKeyBase64, ivBase64);

            // 테스트할 원본 데이터
            String originalText = "AES 암호화 테스트";

            // 암호화
            String encryptedText = AESUtil.encrypt(originalText);
            System.out.println("Encrypted: " + encryptedText);

            // 복호화
            String decryptedText = AESUtil.decrypt(encryptedText);
            System.out.println("Decrypted: " + decryptedText);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
