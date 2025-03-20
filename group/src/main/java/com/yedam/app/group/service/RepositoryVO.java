package com.yedam.app.group.service;

import lombok.Data;

@Data
public class RepositoryVO {
	private int fileRepositoryId;  // 자료실 ID
    private int suberNo;           // 회사 번호
    private Integer departmentNo;  // 부서 번호 (부서 자료실일 경우)
    private Integer employeeNo;    // 사원 번호 (개인 자료실일 경우)
    private String fileRepositoryType; // 자료실 유형 (전체, 부서, 개인
}
