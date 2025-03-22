package com.yedam.app.group.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yedam.app.group.mapper.PaymentMapper;
import com.yedam.app.group.service.PaymentDetailsVO;
import com.yedam.app.group.service.PaymentService;
import com.yedam.app.group.service.PaymentVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{
    private final PaymentMapper paymentMapper;
	@Override
	public void savePayment(PaymentVO payment) {
        paymentMapper.insertPaymentDetail(payment);
	}
	
	@Override
	public List<PaymentVO> findAllpayMent(int suberNo) {
		return paymentMapper.selectAllpayMent(suberNo);
	}
}

