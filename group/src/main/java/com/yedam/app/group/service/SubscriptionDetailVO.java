package com.yedam.app.group.service;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SubscriptionDetailVO {
    private int subDetailsNo;      // 구독상세번호 (PK)
    private int moduleNo;          // 모듈번호 (FK)
    private String subPeriod;      // 구독기간
    private char discount;         // 할인 적용 여부 (Y/N)
    private BigDecimal discountRate;  // 할인율
    private BigDecimal discountPrice; // 할인 후 금액
    private int suberNo;           // 구독자 번호 (FK)
}
