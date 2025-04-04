package com.example.demo;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//@SpringBootTest
public class Pawdtest {

	@Test
	public void test1() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10);
		String hash = bCryptPasswordEncoder.encode("123456");
	}
	
	
}
