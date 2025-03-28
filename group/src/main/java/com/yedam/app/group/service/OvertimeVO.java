package com.yedam.app.group.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data // Lombok: getter, setter, toString, equals, hashCode 자동 생성
public class OvertimeVO {

    // ✅ 초과근무 고유 ID (PK)
    // DB의 OVERTIME 테이블의 기본키 역할
    private Integer overtimeId;

    // ✅ 초과근무 시간 (단위: 분)
    // 예: 120이면 2시간. DB에는 분 단위로 저장됨
    private Integer overtimeTime;

    // ✅ 초과근무 일자
    // 해당 초과근무가 발생한 날짜
    private Date overtimeDate;

    // ✅ 초과근무 종류 (예: 연장근무, 야간근무, 휴일근무 등)
    private String overtimeType;

    // ✅ 해당 초과근무가 연결된 출결기록 ID
    // FOREIGN KEY: work_attitude_id
    private Integer workAttitudeId;

    // ✅ 전자결재 문서 번호 (연동되었을 경우)
    // 초과근무를 결재할 때 사용하는 문서 번호
    private Integer draftDocumentNumber;

    // ✅ [화면 표시용] 시간 단위로 변환해서 보여주는 getter
    // 프론트에서 보기 좋게 "2시간"처럼 표현할 수 있도록 계산
    public double getOvertimeHours() {
        return (overtimeTime != null) ? overtimeTime / 60.0 : 0.0;
    }
}
