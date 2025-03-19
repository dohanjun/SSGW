package com.yedam.app.group.service;

import java.util.Date;

import lombok.Data;

@Data
public class DownloadVO {
	private int downloadNo;
    private int fileId;       // 파일 ID (FK)
    private int employeeNo;   // 다운로드한 직원 번호
    private Date downloadDate;
    private String ip;        // 다운로드한 IP 주소
}
