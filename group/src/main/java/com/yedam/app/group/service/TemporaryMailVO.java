package com.yedam.app.group.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class TemporaryMailVO {

	private Integer employeeId;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date insertDate;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date delDate;
	private int mailId;
	private String temporaryMailBoxNo;
}
