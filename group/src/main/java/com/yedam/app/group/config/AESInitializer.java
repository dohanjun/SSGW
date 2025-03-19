package com.yedam.app.group.config;

import org.springframework.boot.CommandLineRunner;

import com.yedam.app.utill.AESUtil;

public class AESInitializer implements CommandLineRunner{
	private final AESConfig aesConfig;

    public AESInitializer(AESConfig aesConfig) {
        this.aesConfig = aesConfig;
    }

    @Override
    public void run(String... args) throws Exception {
        AESUtil.setKeys(aesConfig.getSecretKey(), aesConfig.getIv());
    }
}
