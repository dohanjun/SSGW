package com.yedam.app.group.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yedam.app.group.service.SubscriberVO;
import com.yedam.app.group.service.SubscriptionSummaryVO;

@Mapper
public interface SubscriberMapper {
    void insertSubscriber(SubscriberVO subscriber);
	List<SubscriptionSummaryVO> selectAllSubers();
	List<SubscriberVO> selectinfoSuber(@Param("suberNo") int suberNo);
	SubscriberVO selectSuber(int suberNo);
	void updateSuber(SubscriberVO vo);
	boolean checkedId(String subId);
	void updateSubscriberPassword(SubscriberVO subscriber);
	int insertTempIp(String tempIp, String employeeId);
}