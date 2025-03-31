package com.yedam.app.group.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface BoardService {
	
	// 게시판 지정
    List<BoardVO> getBoardsByType(String boardType, int suberNo, Integer departmentNo, Integer employeeNo);
    
    // 공지 게시글 조회
    int getNoticeBoardPostCount(int suberNo, String keyword);
    
    List<BoardPostVO> getNoticeBoardPostsPaged(int suberNo, String keyword, int offset, int limit);
    
    // 부서 게시글 조회
    List<BoardPostVO> getDepartmentBoardPostsPaged(Integer suberNo, Integer departmentNo, String keyword, int offset, int limit);
    
    int getDepartmentBoardPostCount(Integer suberNo, Integer departmentNo, String keyword);
    
    // 자유 게시글 조회
    int getFreeBoardPostCount(int suberNo, String keyword);
    
    List<BoardPostVO> getFreeBoardPostsPaged(int suberNo, String keyword, int offset, int limit);
    
    // 게시글 등록 (게시글 + 첨부파일)
    void insertBoardPost(BoardPostVO postVO, String boardType, EmpVO loginUser, List<MultipartFile> files);
    
    BoardPostVO getPostDetail(int postId);
    
    List<BoardAttachmentVO> getAttachments(int postId);
    
    void updateBoardPost(BoardPostVO postVO, List<MultipartFile> files, List<Integer> deleteFileIds);
    
    // 게시글 삭제
    void deleteBoard(int postId);
    
}
