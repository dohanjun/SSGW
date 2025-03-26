package com.yedam.app.group.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class VacationVO {
	private Integer vacationTypeId; 				// 휴가유형
	private String vacationTypeName;				// 휴가이름
	private Integer vacationDate;					// 휴가일수
	private String requiredProofDocumentFile;		// 증빙서류 필요여부
	private Integer suberNo;						// 회사번호
	
	private Integer grantedVacation;		// 부여된 휴가일
	private Integer userVacation;			// 사용한 휴가일
	private Integer remainingVacation;		// 휴가 잔여일
	private Integer employeeNo;				// 사원번호
	private Integer leaveHistoryId;			// 휴가내역번호
	private Date year;						// 연도
	private Integer draftNo;				// 기안문서번호
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date hireDate;			  // 입사일
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date exitDate;			  // 퇴사일
	private String resignationStatus; // 퇴사여부
	
	private int page =1;
	private int size =10;
	private int offset;
	
    private String employeeName;    // 이름 검색
    private Integer departmentNo;    // 부서 검색
    private Integer rankId;          // 직급 검색
    
    private String departmentName;
    private String jobTitleName;
    
    
	
	
	

}
