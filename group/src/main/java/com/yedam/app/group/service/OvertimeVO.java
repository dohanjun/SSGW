package com.yedam.app.group.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class OvertimeVO {
    private Integer overtimeId;
    private Integer overtimeTime; // 분 단위 저장됨
    private Date overtimeDate;
    private String overtimeType;
    private Integer workAttitudeId;
    private Integer draftDocumentNumber;

    // ✅ 화면에서 시간단위로 보여줄 때 사용
    public double getOvertimeHours() {
        return (overtimeTime != null) ? overtimeTime / 60.0 : 0.0;
    }
}
