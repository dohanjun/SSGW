package com.yedam.app.group.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yedam.app.group.service.CommentVO;

@Mapper
public interface CommentMapper {
	// 게시글에 해당하는 댓글 조회
    List<CommentVO> selectCommentsByPostId(int postId);

    // 댓글 추가
    void insertComment(CommentVO comment);

    // 대댓글 추가
    void insertReplyComment(CommentVO comment);

    // 댓글 수정
    void updateComment(@Param("commentId") int commentId, @Param("content") String content);

    // 댓글 삭제
    void deleteComment(int commentId);
    
    CommentVO selectCommentById(int commentId);
}
