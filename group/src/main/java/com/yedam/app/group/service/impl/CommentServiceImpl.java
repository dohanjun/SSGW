package com.yedam.app.group.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yedam.app.group.mapper.CommentMapper;
import com.yedam.app.group.service.CommentService;
import com.yedam.app.group.service.CommentVO;

@Service
public class CommentServiceImpl implements CommentService{
	
	@Autowired
    private CommentMapper commentMapper;

    @Override
    public List<CommentVO> getCommentsByPostId(int postId) {
        return commentMapper.selectCommentsByPostId(postId);
    }

    @Override
    public void addComment(CommentVO comment) {
        commentMapper.insertComment(comment);
    }

    @Override
    public void addReplyToComment(CommentVO replyComment) {
        commentMapper.insertReplyComment(replyComment);
    }

    @Override
    public void editComment(int commentId, String content) {
        commentMapper.updateComment(commentId, content);
    }

    @Override
    public void deleteComment(int commentId) {
        commentMapper.deleteComment(commentId);
    }
}
