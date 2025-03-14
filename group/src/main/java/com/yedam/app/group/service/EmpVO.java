package com.yedam.app.group.service;

import java.util.Date;

import lombok.Data;

@Data
public class EmpVO {
	private Integer employeeNO;		  // 사원번호
	private String employeeName;	  // 사원이름
	private String employeeId;        // 사원 아이디
	private String employeePw;     	  // 사원 비밀번호
	private String resignationStayus; // 퇴사여부
	private String tempIp;			  // 예비Ip
	private String passwordChanged;   // 비밀번호 변경여부
	private Date hireDate;			  // 입사일
	private Date exitDate;			  // 퇴사일
	private String phoneNumber;	 	  // 연락처
	private String address;			  // 주소
	private String profileImage;	  // 프로필 이미지
	private int suberNo;			  // 회사번호
	private int rankId;			      // 직급Id
	private int rightsId;			  // 권한Id 
	private int manager;			  // 매니저Id 
	

}
