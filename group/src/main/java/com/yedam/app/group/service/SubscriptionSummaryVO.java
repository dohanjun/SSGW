package com.yedam.app.group.service;

import lombok.Data;

@Data
public class SubscriptionSummaryVO {
    private int suberNo;              // 회사번호
    private String subName;           // 회사명
    private String moduleName;        // 구독중인 모듈 (여러 모듈명을 쉼표로 구분하여 결합)
    private String paymentStatus;     // 결제 상태
    private String paymentEndDate;    // 결제 종료일시 (예: "2025년 03월 19일")
}
