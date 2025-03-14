package com.yedam.app.group.service;

import lombok.Data;

@Data
public class DeptVO {
	private Integer departmentNo; 		// 부서번호
	private int upperDepNo; 			// 상위 부서번호
	private String departmentName;		// 부서이름
	private String departmentLevel;		// 부서레벨
	private int manager;				// 매니저 ID
	private int suberNo;				// 회사번호

}
