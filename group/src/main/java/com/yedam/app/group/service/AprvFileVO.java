package com.yedam.app.group.service;

import lombok.Data;

@Data
public class AprvFileVO {
	private Integer fileId;
	private int draftNo;
	private String fileName;
	private String filePath;
	private Long fileSize;
	private int fileOrder;
}
