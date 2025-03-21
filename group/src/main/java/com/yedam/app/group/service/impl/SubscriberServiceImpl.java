package com.yedam.app.group.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yedam.app.group.mapper.SubscriberMapper;
import com.yedam.app.group.service.SubscriberService;
import com.yedam.app.group.service.SubscriberVO;
import com.yedam.app.group.service.SubscriptionSummaryVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubscriberServiceImpl implements SubscriberService {
    
    private final SubscriberMapper subscriberMapper;

    @Override
    public int saveSubscriber(SubscriberVO subscriber) {
        subscriberMapper.insertSubscriber(subscriber);
        return subscriber.getSuberNo();
    }

	@Override
	public List<SubscriptionSummaryVO> findAllSubscribers() {
		return subscriberMapper.selectAllSubers();
	}

	@Override
	public List<SubscriberVO> findinfoSuberByNo(int suberNo) {
		return subscriberMapper.selectinfoSuber(suberNo);
	}

}