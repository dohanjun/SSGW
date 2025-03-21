package com.yedam.app.group.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yedam.app.group.mapper.PaymentDetailsMapper;
import com.yedam.app.group.service.PaymentDetailsService;
import com.yedam.app.group.service.PaymentDetailsVO;

@Service
public class PaymentDetailsServiceImpl implements PaymentDetailsService {
    
    @Autowired
    private PaymentDetailsMapper paymentDetailsMapper;

	@Override
	public List<PaymentDetailsVO> createPaymentDetails(List<PaymentDetailsVO> list) {
        for (PaymentDetailsVO vo : list) {
            paymentDetailsMapper.insertPaymentDetails(vo);
        }
        return list;
	}
}