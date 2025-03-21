package com.yedam.app.group.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.yedam.app.group.service.PaymentDetailsVO;

@Mapper
public interface PaymentDetailsMapper {
    void insertPaymentDetails(PaymentDetailsVO details);
}