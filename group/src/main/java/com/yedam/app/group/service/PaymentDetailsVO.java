package com.yedam.app.group.service;

import java.util.Date;
import lombok.Data;

@Data
public class PaymentDetailsVO {
    private Integer paymentDetailsNo; // ✅ 결제 상세 번호 (PK, 자동 증가)
    private Integer subDetailsNo;     // ✅ 구독 상세 번호 (FK, NULL 허용)
    private Integer paymentNo;        // ✅ 결제 번호 (FK, NULL 허용)
    private Integer paymentPrice;     // ✅ 결제 금액 (NOT NULL)
    private String paymentState;      // ✅ 결제 상태 (VARCHAR2(20), NOT NULL)
    private Date paymentDate;         // ✅ 결제 날짜 (DATE, NOT NULL)
    private String impUid;
}
