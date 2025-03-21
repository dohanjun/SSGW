package com.yedam.app.group.service;

import java.util.Date;

import lombok.Data;


@Data
public class SubscriberVO {
	//회사 테이블
    private int suberNo;         // 회사번호
    private String subId;        // 구독자 아이디
    private String subPw;        // 구독자 비밀번호
    private String subUid;       // 구독자 UID
    private String subName;      // 구독자 이름
    private String subEmail;     // 구독자 이메일
    private String subBnisNo;    // 구독자 사업자번호
    private int maxCount;        // 최대 인원
    private Integer delayPeriod; // 유예기간
    private Integer currentCount;// 현재 인원 
    private double maxSize;      // 최대 용량
    private String companyName;  // 회사 이름
    private String firstIp;      // IP 1
    private String secondIp;     // IP 2 
    private String state;        // 상태
    private Date delDay;         // 데이터 삭제일 
    private String domain;       // 도메인
    private Character locked;    // 잠김 여부 
    private double maxUpSize;
    
    private int paymentNo;               // 결제번호
    private Date paymentDate;            // 결제일
    private String paymentType;          // 결제수단
    private double paymentPrice;         // 총결제금액
    private String paymentStatus;        // 결제상태
    private Date invoiceDate;            // 청구서발행일
    private String overduePeriod;        // 연체여부
    private String claim;                // 청구여부
    private String claimState;           // 청구상태
    private String overdueDay;           // 연체기간
    private Date paymentStartDate;       // 결제시작일
    private Date paymentEndDate;         // 결제종료일
    private int subPeriod;               // 모듈구독기간
    private String cancelState;          // 해지신청여부
    private Date cancelDate;             // 해지신청일
    
    private int moduleNo;       // 모듈 번호
    private String moduleName;  // 모듈 이름
    private double modulePrice; // 1달 모듈 가격
    private String version;     // 버전
    private char basicModule;   // 기본 모듈 여부 (Y/N)
    private char activate;      // 활성화 여부 (Y/N)
    private String explanation; // 모듈 설명
    
    private Integer paymentDetailsNo; // ✅ 결제 상세 번호 (PK, 자동 증가)
    private Integer subDetailsNo;     // ✅ 구독 상세 번호 (FK, NULL 허용)
    private String paymentState;      // ✅ 결제 상태 (VARCHAR2(20), NOT NULL)
}
