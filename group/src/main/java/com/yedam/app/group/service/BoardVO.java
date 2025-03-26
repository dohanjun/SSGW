package com.yedam.app.group.service;

import lombok.Data;

@Data
public class BoardVO {
	private int boardId;           // 게시판 ID
    private String boardType;      // 공지, 부서, 자유
    private String boardName;      // 게시판 이름 (예: 부서 게시판, 공지 게시판)
    private String boardState;     // 사용중 / 사용안함 등
    private int suberNo;           // 회사 번호

    // 조건에 따라 들어올 수 있는 값들
    private Integer departmentNo;  // 부서 번호 (부서 게시판일 경우)
    private Integer employeeNo;    // 사원 번호 (자유 게시판일 경우)
}
