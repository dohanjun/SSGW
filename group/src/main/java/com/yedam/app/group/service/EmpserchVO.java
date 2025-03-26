package com.yedam.app.group.service;

import lombok.Data;

@Data
public class EmpserchVO {
	private int page =1;
	private int size =10;
	private int offset;
	private String employeeName;
	private String departmentNo;
	private String rankId;
	private int suberNo;
	private String resignationStatus;
}
