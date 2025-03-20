package com.yedam.app.group.service;

import lombok.Data;

@Data
public class RankVO {
	private String jobTitleName;		// 직급명  
	private Integer jobTitleLevel;			// 직급레벨
	private Integer rankId;					// 직급Id
	private Integer suberNo;				// 회사번호

}
