package com.yedam.app.group.service;

import java.util.Date;

import lombok.Data;

@Data
public class RepositoryFileVO {
	private int fileId;
    private String fileName;
    private String filePath; // 암호화 대상
    private Date creationDate;
    private long fileSize;
    private String fileExtension;
    private int writingId;
    private String saveFileName; // 암호화 대상
}
