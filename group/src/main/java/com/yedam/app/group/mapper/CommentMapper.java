package com.yedam.app.group.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.yedam.app.group.service.CommentVO;

@Mapper
public interface CommentMapper {
	// 게시글에 해당하는 댓글 조회
    List<CommentVO> selectCommentsByPostId(int postId);

    // 댓글 추가
    void insertComment(CommentVO commentVO);

    // 대댓글 추가
    void insertReplyComment(CommentVO commentVO);

    // 댓글 수정
    void updateComment(int commentId, String content);

    // 댓글 삭제
    void deleteComment(int commentId);
}
