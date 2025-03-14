package com.yedam.app.group.service;

import lombok.Data;

@Data
public class RankVO {
	private String jobTitleName;		// 직급명  
	private int jobTitleLevel;			// 직급레벨
	private int rankId;					// 직급Id
	private int suberNo;				// 회사번호

}
