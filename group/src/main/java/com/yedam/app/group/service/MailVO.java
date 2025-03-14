package com.yedam.app.group.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class MailVO {
	private int mailId;
	private String title;
	private String content;
	private int sentState;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date sentDate;
	private Integer employeeId;
	private String attachedFileName;
	private String getUser;
	private String cc;
	private String recode;
	private String mailType;
}
