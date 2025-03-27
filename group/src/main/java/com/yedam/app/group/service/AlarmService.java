package com.yedam.app.group.service;

import java.util.List;

public interface AlarmService {
    List<AlarmVO> getUnreadAlarmsByEmployeeNo(int employeeNo);

	boolean markAsRead(int alertNo);
	public void insertAlarm(AlarmVO vo);
}
