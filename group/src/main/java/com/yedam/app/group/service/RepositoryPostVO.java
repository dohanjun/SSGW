package com.yedam.app.group.service;

import java.util.Date;

import lombok.Data;

@Data
public class RepositoryPostVO {
	private int writingId;
    private String title;
    private String cotent;
    private Date creationDate;
    private int employeeNo;
    private int fileRepositoryId;
    private char fix;
}
