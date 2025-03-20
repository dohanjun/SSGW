package com.yedam.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.yedam.app"})
@MapperScan(basePackages="com.yedam.app.**.mapper")
public class GroupApplication {
	public static void main(String[] args) {
		SpringApplication.run(GroupApplication.class, args);
	}

}
