package com.yedam.app.group.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yedam.app.group.mapper.AlarmMapper;
import com.yedam.app.group.service.AlarmService;
import com.yedam.app.group.service.AlarmVO;

@Service
public class AlarmServiceImpl implements AlarmService {

    @Autowired
    private AlarmMapper alarmMapper;

    @Override
    public List<AlarmVO> getUnreadAlarmsByEmployeeNo(int employeeNo) {
        List<AlarmVO> alarms = alarmMapper.getUnreadAlarmsByEmployeeNo(employeeNo);
        return alarms;
    }

	@Override
	public boolean markAsRead(int alertNo) {
	    return alarmMapper.updateReadStatus(alertNo);
	}
    @Override
    public void insertAlarm(AlarmVO vo) {
        alarmMapper.insertAlarm(vo);
    }
}

