package com.yedam.app.group.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ApprovalVO {
	private Integer stampId;
	private int employeeNo;
	private String stampImgPath;
	private String active;
	private int stampOrder;
	
	// 기본양식
	private Integer basicsFormId;
	private String content;
	private int version;
	private String remarks;
	private String formType;
	
	// 전자결재
	private Integer draftNo;      // 문서번호
	private String title;         // 제목
	private String aprvStatus;    // 결재상태
	private String rejectReason;  // 반려사유
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private Date draftDate;       // 상신일
	private Date aprvDate;        // 결재일
	private int formId;           // 양식번호
	private int suberNo;          // 회사번호
	
	private String employeeName;
	
}
