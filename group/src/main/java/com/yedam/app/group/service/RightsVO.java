package com.yedam.app.group.service;

import lombok.Data;

@Data
public class RightsVO {
	private String rightsName;			// 권한명
	private Integer rightsId;			// 권한Id
	private Integer suberNo;			// 회사번호
	private Integer rightsLevel;		// 권한레벨

}
