package com.yedam.app.group.service.impl;

import org.springframework.stereotype.Service;

import com.yedam.app.group.mapper.SubscriberMapper;
import com.yedam.app.group.service.SubscriberService;
import com.yedam.app.group.service.SubscriberVO;

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

}