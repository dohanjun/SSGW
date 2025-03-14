package com.yedam.app.group.service;

import java.util.Date;

import lombok.Data;


@Data
public class SubscriberVO {
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
}
