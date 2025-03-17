package com.yedam.app.group.service;

import java.util.Date;

import lombok.Data;

@Data
public class ApprovalFormVO {
	private Integer formId;     // 양식 id
	private String formType;    // 양식유형
	private String remarks;     // 비고
	private String content;     // 내용
	private int version;        // 버전
	private String active;      // 활성화 여부
	private Date creationDate;  // 생성일
	private int suberNo;        // 회사번호
}
