package com.yedam.app.group.service;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class DownloadVO {
	private Long downloadNo;
    private Long fileId;       // 파일 ID (FK)
    private Integer employeeNo;   // 다운로드한 직원 번호
    private Timestamp downloadDate;
    private String ip;        // 다운로드한 IP 주소
}
