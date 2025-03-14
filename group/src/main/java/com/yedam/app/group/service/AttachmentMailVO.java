package com.yedam.app.group.service;

import lombok.Data;

@Data
public class AttachmentMailVO {

	private int attachedFileId;
	private String fileName;
	private String filePath;
	private int fileSize;
	private int mailId;
}
