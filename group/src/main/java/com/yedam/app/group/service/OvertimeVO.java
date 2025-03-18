package com.yedam.app.group.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class OvertimeVO {
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date overtimeDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date overtimeTime;
	private String overtimeType;
	private Integer overtimeId;
	private int workAttitudeId;
	private int draftDocumentNumber;
}
