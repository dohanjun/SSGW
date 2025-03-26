package com.yedam.app.group.service;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class PaymentVO {
    private int paymentNo;               // 결제번호
    private Date paymentDate;            // 결제일
    private String paymentType;          // 결제수단
    private double paymentPrice;         // 총결제금액
    private String paymentStatus;        // 결제상태
    private Date invoiceDate;            // 청구서발행일
    private String overduePeriod;        // 연체여부
    private int suberNo;                 // 회사번호
    private String claim;                // 청구여부
    private String claimState;           // 청구상태
    private String overdueDay;           // 연체기간
    private Date paymentStartDate;       // 결제시작일
    private Date paymentEndDate;         // 결제종료일
    private int subPeriod;               // 모듈구독기간
    private String cancelState;          // 해지신청여부
    private Date cancelDate;             // 해지신청일
    
    List<PaymentDetailsVO> paymentDetailsList; 

}