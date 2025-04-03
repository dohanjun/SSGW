package com.yedam.app.group.service;

import java.util.Date;

import lombok.Data;

@Data
public class DeptHistVO {
	private Date moveDate;					// 이동날짜
	private String content;					// 내용
	private Date deleteDate;				// 삭제날짜
	private Integer employeeNo;				// 사원번호
	private Integer departmentNo;				// 부서번호
	private Integer movedToDepartment;			// 이동한부서
	private Integer departmentTransferId;	// 부서이동ID
	private Integer currentRankId;			// 이동 후 직책 ID
	private Integer previousRankId;			// 이동 전 직책 ID
	
	// 추가 필드들
    private String employeeName;        // 사원 이름
    private String previousRankName;    // 이전 직책 이름
    private String currentRankName;     // 현재 직책 이름
    private String previousDeptName;    // 이전 부서 이름
    private String movedToDeptName;     // 이동한 부서 이름
    private Integer suberNo;			// 회사번호
   
    private Integer rankId;         	// 현재 직급
    
    
    private int page = 1;         		// 현재 페이지
    private int size = 10;        		// 페이지당 출력 수
    private int offset = 0;       		// OFFSET
    private int start;
    private int amount;
    
    private String resignationStatus;	// 재직 퇴직 구분
    
    

	

}
