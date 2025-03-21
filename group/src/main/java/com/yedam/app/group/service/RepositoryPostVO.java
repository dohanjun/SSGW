package com.yedam.app.group.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class RepositoryPostVO {
	private Long writingId;
    private String title;
    private String content;
    private String fileName;
    private int fileCount;
    private String writer;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date creationDate;
    private int employeeNo;
    private int fileRepositoryId;
    private char fix;
}
