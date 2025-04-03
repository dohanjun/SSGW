package com.yedam.app.group.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yedam.app.group.service.BoardAttachmentVO;
import com.yedam.app.group.service.BoardPostVO;
import com.yedam.app.group.service.BoardVO;

@Mapper
public interface BoardMapper {

    // 게시글 등록
    void insertPost(BoardPostVO postVO);

    // 첨부파일 등록
    void insertAttachment(BoardAttachmentVO attach);

    // 게시판 종류별 목록 조회
    List<BoardVO> selectBoardsByType(
    	    @Param("boardType") String boardType,
    	    @Param("suberNo") int suberNo,
    	    @Param("departmentNo") Integer departmentNo,
    	    @Param("employeeNo") Integer employeeNo
    	);

    // 공지 게시판
    int countNoticeBoardPosts(int suberNo, String keyword);
    List<BoardPostVO> selectNoticeBoardPostsPaged(int suberNo, String keyword, int offset, int limit);

    // 부서 게시판
    int countDepartmentBoardPosts(Integer suberNo, Integer departmentNo, String keyword);
    List<BoardPostVO> selectDepartmentBoardPostsPaged(Integer suberNo, Integer departmentNo, String keyword, int offset, int limit);

    // 자유 게시판
    int countFreeBoardPosts(int suberNo, String keyword);
    List<BoardPostVO> selectFreeBoardPostsPaged(int suberNo, String keyword, int offset, int limit);
    
    void increaseViewCount(int postId);
    
    BoardPostVO selectPostById(int postId);
    
    List<BoardAttachmentVO> getAttachments(int postId);
    
    BoardAttachmentVO getBoardAttachmentById(int attachmentId);
    
    List<BoardAttachmentVO> selectBoardAttachmentsByPostId(int postId);
    
    void updateBoardPost(BoardPostVO postVO);
    
    void deleteAttachmentById(int attachmentId);
    
    void deleteBoard(int postId);
    
    void updateFixStatus(@Param("postId") int postId, @Param("fixed") String fixed);
    
    List<BoardPostVO> getFixedNoticeBoardPosts(int suberNo);

    List<BoardPostVO> getNoticeBoardPostsPaged(@Param("suberNo") int suberNo,
                                               @Param("keyword") String keyword,
                                               @Param("offset") int offset,
                                               @Param("pageSize") int pageSize);

    List<BoardPostVO> getFixedDepartmentBoardPosts(@Param("suberNo") int suberNo,
                                                   @Param("departmentNo") int departmentNo);

    List<BoardPostVO> getDepartmentBoardPostsPaged(@Param("suberNo") int suberNo,
                                                   @Param("departmentNo") int departmentNo,
                                                   @Param("keyword") String keyword,
                                                   @Param("offset") int offset,
                                                   @Param("pageSize") int pageSize);
    
    int countFixedPosts(@Param("boardId") int boardId);

}
