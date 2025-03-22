package com.yedam.app.group.service;

import java.util.List;

public interface PaymentService {
	void savePayment(PaymentVO payment);
	
	List<PaymentVO> findAllpayMent(int suberNo);
}
