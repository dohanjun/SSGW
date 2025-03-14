package com.yedam.app.group.service;

import lombok.Data;

@Data
public class RightsVO {
	private String rightsName;		// 권한명
	private int rightsId;			// 권한Id
	private int suberNo;			// 회사번호
	private int rightsLevel;		// 권한레벨

}
