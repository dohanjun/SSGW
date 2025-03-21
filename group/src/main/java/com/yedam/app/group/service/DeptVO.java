package com.yedam.app.group.service;

import lombok.Data;

@Data
public class DeptVO {
	private Integer departmentNo; 			// 부서번호
	private Integer upperDepNo; 			// 상위 부서번호
	private String departmentName;			// 부서이름
	private String departmentLevel;			// 부서레벨
	private Integer manager;				// 매니저 ID
	private Integer suberNo;				// 회사번호
	

}
