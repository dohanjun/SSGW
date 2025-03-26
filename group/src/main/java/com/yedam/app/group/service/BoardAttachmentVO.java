package com.yedam.app.group.service;

import java.util.Date;

import lombok.Data;

@Data
public class BoardAttachmentVO {
	private int attachmentId;         // 첨부파일 ID
    private int postId;               // 게시글 ID (FK)
    private String filePath;          // 파일 경로
    private String fileTitle;         // 원본 파일명
    private Date fileRetentionPeriod; // 파일 유지일자
}
