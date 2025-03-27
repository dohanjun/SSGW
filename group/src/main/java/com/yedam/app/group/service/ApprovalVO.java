package com.yedam.app.group.service;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ApprovalVO {
	private Integer stampId;       // 도장아이디
	private Integer employeeNo;    // 사원번호
	private String stampImgPath;   // 이미지경로
	private String active;         // 활성화 여부 '0', '1'
	private int stampOrder;        // 순서
	
	// 기본양식
	private Integer basicsFormId;  // 기본양식아이디
	private String content;        // 양식
	private int version;           // 버전
	private String remarks;        // 비고
	private String formType;       // 유형
	
	// 전자결재
	private Integer draftNo;      // 문서번호
	private String title;         // 제목
	private String aprvStatus;    // 결재상태
	private String rejectReason;  // 반려사유
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date draftDate;       // 상신일
	private Date aprvDate;        // 결재일
	private Integer formId;       // 양식번호
	private int suberNo;          // 회사번호
	
	private String employeeName;  // 사원이름
	private String aprvRole;
	
	private List<Integer> approvers; // 결재자
	private List<Integer> references; // 참조자
	
	private int page = 1;            // 페이지
	private int size = 10;
	private int offset;
	
	private String startDate;        // 휴가 시작일
	private String endDate;          // 휴가 종료일
	private String usedVacation;     // 휴가 사용일
}
