package com.yedam.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.yedam.app"})
@MapperScan(basePackages="com.yedam.app.**.mapper")
@EnableScheduling  // 스케줄링 기능 활성화
public class GroupApplication {
	public static void main(String[] args) {
		SpringApplication.run(GroupApplication.class, args);
	}

}
