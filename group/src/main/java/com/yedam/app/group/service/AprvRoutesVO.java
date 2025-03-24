package com.yedam.app.group.service;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class AprvRoutesVO {
	
	private Integer aprvId;       // 결재 아이디
	private String aprvOrder;     // 결재순서
	private Date aprvDate;        // 결재일
	private String rejectReason;  // 반려사유
	private int draftNo;          // 기안문서번호
	private String aprvRole;      // 역할(결재, 참조)
	private String aprvStatus;    // 결재상태(대기, 진행, 완료, 반려)
	private int employeeNo;       // 결재자 사원번호
	private Integer stampId;          // 결재자 도장 id
	private int rankId;           // 직급아이디
	private String jobTitleName;     // 직급명
	private String employeeName;  // 사원이름
	private int suberNo;          // 회사번호
	private String stampImgPath;  // 도장이미지경로
	
	private List<Integer> approvers; // 결재자 목록
	private List<Integer> references; // 참조자 목록
}
