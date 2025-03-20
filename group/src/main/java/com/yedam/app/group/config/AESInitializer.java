package com.yedam.app.group.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.yedam.app.utill.AESUtil;

@Component
public class AESInitializer implements CommandLineRunner {
    private final AESConfig aesConfig;

    public AESInitializer(AESConfig aesConfig) {
        this.aesConfig = aesConfig;
    }

    @Override
    public void run(String... args) {
        AESUtil.setKeys(aesConfig.getSecretKey(), aesConfig.getIv());
        System.out.println("✅ AES Keys Initialized Successfully!");
    }
}
