package com.yedam.app.group.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ReceiveReferenceVO {
	private String receiveMail;
	private int receiveRead;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date receiveTime;
	private String ccMail;
	private int ccRead;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date ccTime;
	private int mailId;
	private int receiveCcNo;
}
