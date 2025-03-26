package com.yedam.app.group.service;

import java.util.List;

public interface SubscriberService {
    int saveSubscriber(SubscriberVO subscriber);
    List<SubscriptionSummaryVO> findAllSubscribers();
    List<SubscriberVO> findinfoSuberByNo(int suberNo);
    SubscriberVO findinfoSuber(int suberNo);
	void updateSuber(SubscriberVO vo);
	boolean isSubIdExists(String subId);
	void updateSubscriberPassword(SubscriberVO subscriber);
	int insertTempIp(String tempIp, String employeeId);
}
