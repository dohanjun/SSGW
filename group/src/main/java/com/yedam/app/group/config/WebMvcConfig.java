package com.yedam.app.group.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // "/uploads/**" 요청이 오면 "D:/uploads/" 디렉토리에서 파일을 찾도록 설정
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:D:/uploads/");
        		// => 하나의 URL에 여러 경로를 매핑하는 게 가능함
        		// file:///D:/upload/
    }
}
