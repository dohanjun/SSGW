package com.yedam.app.group.service;

import java.util.Date;

import lombok.Data;

@Data
public class BasketVO {
	private int basketId;
    private Date delDate;   // 삭제일자 (휴지통 이동일)
    private int writingId;  // 게시글 ID
    private String title;
    private String writer;
    private Date creationDate;
    private String fileName;
    
    private String repositoryType;
    private int fileCount;
}

