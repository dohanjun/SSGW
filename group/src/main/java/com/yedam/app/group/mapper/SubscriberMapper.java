package com.yedam.app.group.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.yedam.app.group.service.SubscriberVO;
import com.yedam.app.group.service.SubscriptionSummaryVO;

@Mapper
public interface SubscriberMapper {
    void insertSubscriber(SubscriberVO subscriber);
	List<SubscriptionSummaryVO> selectAllSubers();
	List<SubscriberVO> selectinfoSuber(int suberNo);
}