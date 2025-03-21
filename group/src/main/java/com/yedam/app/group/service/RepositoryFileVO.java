package com.yedam.app.group.service;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class RepositoryFileVO {
	private int fileId;
    private String fileName;
    private String filePath; // 암호화 대상
    private Timestamp creationDate;
    private Long fileSize;
    private String fileExtension;
    private Long writingId;
    private String saveFileName; // 암호화 대상
}
