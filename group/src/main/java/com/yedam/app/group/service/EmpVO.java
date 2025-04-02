package com.yedam.app.group.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

/**
 * 사원 정보 VO
 */
@Data
public class EmpVO {
	
	// 기본 정보
	private Integer employeeNo;		  		// 사원번호
	private String employeeName;	  		// 사원이름
	private String employeeId;        		// 사원 아이디
	private String employeePw;     	  		// 사원 비밀번호
	private String resignationStatus; 		// 퇴사여부
	private String tempIp;			  		// 예비Ip
	private String passwordChanged;   		// 비밀번호 변경여부
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date hireDate;			  		// 입사일
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date exitDate;			  		// 퇴사일
	private String phoneNumber;	 	  		// 연락처
	private String address;			 		// 주소
	
	// 이미지 관련
	private String profileImage;	  		// 프로필 이미지 // BLOB 방식 밑에 추가해서 이거안씀
    private byte[] profileImageBLOB; 		//  이미지 (숫자로 변환되어 저장됨)
    private MultipartFile profileImageFile; //  사용자가 업로드한 파일
    private String profileImageBase64; 		// 	조회용 base64 이미지 문자열
    
    // 소속 정보
    private Integer suberNo;			  	// 회사번호
    private Integer departmentNo;		 	// 부서번호
    private Integer rankId;			     	// 직급Id
    private Integer rightsId;			 	// 권한Id 
    private Integer manager;			  	// 매니저Id
    
    // 조회용 정보   
	private String departmentName;			// 부서이름
	private String jobTitleName;			// 직급명
	private Integer jobTitleLevel;			// 직급레벨	
	private String rightsName;				// 권한명
	private Integer rightsLevel;			// 권한 레벨
	
	
	private Double totalWorkingHours;		// 전체근무시간
	private Double overtimeHours;			// 초과근무

}
