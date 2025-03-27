package com.yedam.app.group.service;

import java.util.Date;

import lombok.Data;

@Data
public class AlarmVO {
    private int alarmNo;           // ALARM_NO
    private String alarmMessage;   // ALARM_MASSAGE
    private String alarmType;      // ALARM_TYPE
    private Date createDate;       // CREATE_DATE
    private String read;           // READ
    private int employeeNo;        // EMPLOYEE_NO
    private String alarmIcon;      // ALARM_ICON (추가된 아이콘 필드)
}
