package com.yedam.app.group.service;

import lombok.Data;

@Data
public class EmpserchVO {
	private Integer page =1;
	private Integer size =10;
	private Integer offset;
	private String employeeName;
	private Integer departmentNo;
	private Integer rankId;
	private Integer suberNo;
	private String resignationStatus;
}
