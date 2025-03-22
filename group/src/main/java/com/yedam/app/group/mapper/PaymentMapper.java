package com.yedam.app.group.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.yedam.app.group.service.PaymentVO;

@Mapper
public interface PaymentMapper{
	void insertPaymentDetail(PaymentVO payments);

	List<PaymentVO> selectAllpayMent(int suberNo);
}
