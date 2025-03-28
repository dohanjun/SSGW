package com.yedam.app.group.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MailCleanup {

	@Scheduled(cron = "0 0 0 * * ?")
	public void deleteTemporaryMails() {
		System.out.println("임시 메일 삭제 작업 시작...");
	}
}
