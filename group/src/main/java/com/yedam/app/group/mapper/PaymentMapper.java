package com.yedam.app.group.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.yedam.app.group.service.PaymentVO;

@Mapper
public interface PaymentMapper{
	void insertPaymentDetail(PaymentVO payments);
}
