package com.yedam.app.group.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class OvertimeVO {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date overtimeDate;
    
    private int overtimeTime; // ✅ "분 단위"로 저장되는 숫자 타입
    private String overtimeType;
    private Integer overtimeId;
    private Integer workAttitudeId;
    private int draftDocumentNumber;

    // ✅ 초과 근무 시간을 "시간"으로 변환
    public double getOvertimeHours() {
        return Math.round(overtimeTime / 60.0); // 분을 시간 단위로 변환
    }


}
