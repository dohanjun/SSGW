package com.yedam.app.group.service;

import java.util.Date;

import lombok.Data;

@Data
public class CommentVO {
	private int commentId;        // 댓글 ID
    private Integer reComment;     // 대댓글 ID
    private String content;       // 댓글 내용
    private Date boardDate;       // 댓글 작성일
    private int employeeNo;       // 댓글 작성자 사원번호
    private int postId;           // 게시글 ID
    
    private String employeeName;     // 작성자 이름
    private String departmentName;   // 부서명
}
