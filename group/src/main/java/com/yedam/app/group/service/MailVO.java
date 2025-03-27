package com.yedam.app.group.service;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class MailVO {
	private Integer mailId;
	private String title;
	private String content;
	private int sentState;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date sentDate;
	private String employeeId;
	private String attachedFileName;
	private String getUser;
	private String cc;
	private String recode;
	private String mailType;
	
	private String HttpSession;
	
    private String id;
    
    private boolean isTemporary;
    private LocalDateTime expiryDate;
    
    private String isSent;
}
