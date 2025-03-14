package com.yedam.app.group.service;

import java.util.Date;

import lombok.Data;

@Data
public class DeptHistVO {
	private Date moveDate;					// 이동날짜
	private String content;					// 내용
	private Date deleteDate;				// 삭제날짜
	private int employeeNo;					// 사원번호
	private int departmentNo;				// 부서번호
	private int movedToDepartment;			// 이동한부서
	private Integer departmentTransferId;	// 부서이동ID
	

}
