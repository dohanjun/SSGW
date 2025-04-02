package com.yedam.app.group.service.impl;

import java.util.Base64;
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
    
    @Override
    public CommentVO getCommentById(int commentId) {
        return commentMapper.selectCommentById(commentId);
    }
    
    @Override
    public List<CommentVO> getParentComments(int postId, int offset, int limit) {
        List<CommentVO> list = commentMapper.getParentComments(postId, offset, limit);

        for (CommentVO vo : list) {
            if (vo.getProfileImageBLOB() != null) {
                String base64 = Base64.getEncoder().encodeToString(vo.getProfileImageBLOB());
                vo.setProfileImageBase64(base64);
            }
        }

        return list;
    }

    @Override
    public List<CommentVO> getReplyComments(int postId) {
        List<CommentVO> replies = commentMapper.getReplyComments(postId);

        for (CommentVO vo : replies) {
            if (vo.getProfileImageBLOB() != null) {
                String base64 = Base64.getEncoder().encodeToString(vo.getProfileImageBLOB());
                vo.setProfileImageBase64(base64);
            }
        }

        return replies;
    }

    @Override
    public int countParentComments(int postId) {
        return commentMapper.countParentComments(postId);
    }
}
