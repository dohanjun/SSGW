package com.yedam.app.group.service;

import java.util.List;

public interface CommentService {
	// 게시글에 해당하는 댓글 조회
    List<CommentVO> getCommentsByPostId(int postId);

    // 댓글 추가
    void addComment(CommentVO comment);

    // 대댓글 추가
    void addReplyToComment(CommentVO replyComment);

    // 댓글 수정
    void editComment(int commentId, String content);

    // 댓글 삭제
    void deleteComment(int commentId);
    
    CommentVO getCommentById(int commentId);
    
    List<CommentVO> getParentComments(int postId, int offset, int limit);
    
    List<CommentVO> getReplyComments(int postId);
    
    int countParentComments(int postId);
}
