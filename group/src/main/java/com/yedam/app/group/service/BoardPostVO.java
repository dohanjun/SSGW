package com.yedam.app.group.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class BoardPostVO {
    private int postId;             // 글 ID
    private Integer parentCommentId; // 원글 ID (NULL 허용)
    private String postTitle;       // 제목
    private String postContent;     // 내용
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date postDate;          // 등록 날짜
    private char fixed;             // 고정 여부 (Y/N)
    private char faq;               // FAQ 여부 (Y/N)
    private String location;        // 위치 (NULL 허용)
    private char read;              // 읽은 여부 (Y/N)
    private int boardId;            // 게시판 ID
    private int employeeNo;         // 사원 번호
    private String employeeName;         // 사원 이름
    private String childYn;
}