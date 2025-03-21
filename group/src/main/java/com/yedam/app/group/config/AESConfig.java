package com.yedam.app.group.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AESConfig {

	@Value("${aes.secret.key}")
    private String secretKeyBase64;

    @Value("${aes.iv}")
    private String ivBase64;

    public String getSecretKey() {
        return secretKeyBase64; // 🔹 Base64 인코딩된 값 그대로 반환
    }

    public String getIv() {
        return ivBase64; // 🔹 Base64 인코딩된 값 그대로 반환
    }
}
