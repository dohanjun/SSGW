package com.yedam.app.group.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.yedam.app.group.service.SubscriberVO;

@Mapper
public interface SubscriberMapper {
    void insertSubscriber(SubscriberVO subscriber);
}