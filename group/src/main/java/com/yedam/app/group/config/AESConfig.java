package com.yedam.app.group.config;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AESConfig {

    @Value("${aes.secret.key}")
    private String secretKeyBase64;

    @Value("${aes.iv}")
    private String ivBase64;

    public byte[] getSecretKey() {
        return Base64.getDecoder().decode(secretKeyBase64); //Base64 디코딩
    }

    public byte[] getIv() {
        return Base64.getDecoder().decode(ivBase64); // Base64 디코딩
    }
}
