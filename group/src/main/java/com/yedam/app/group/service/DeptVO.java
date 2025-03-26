package com.yedam.app.group.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class DeptVO {
	// DEPARTMENT 테이블
	private Integer departmentNo; 			// 부서번호
	private Integer upperDepNo; 			// 상위 부서번호
	private String departmentName;			// 부서이름
	private String departmentLevel;			// 부서레벨
	private Integer manager;				// 매니저 ID
	private Integer suberNo;				// 회사번호
	
	// SubscriberVO
	private String companyName;  // 회사 이름
	
	// EMPLOYEES 테이블
	private List<EmpVO> employees;
	private Integer employeeNo;		  		// 사원번호
	private String employeeName;	 		// 사원이름
    private byte[] profileImageBLOB; 		//  이미지 (숫자로 변환되어 저장됨)
    private MultipartFile profileImageFile; //  사용자가 업로드한 파일
    
    // RANK 테이블
	private String jobTitleName;			// 직급명  
	private Integer jobTitleLevel;			// 직급레벨
	private Integer rankId;					// 직급Id
	
	//
	private Integer employeeCount; 			// 부서 총원 (계산용, DB에는 없음)
	

}
