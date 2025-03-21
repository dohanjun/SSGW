package com.yedam.app.group.service.impl;

import org.springframework.stereotype.Service;

import com.yedam.app.group.mapper.BoardPostMapper;
import com.yedam.app.group.service.BoardPostService;
import com.yedam.app.group.service.BoardPostVO;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Service
public class BoardPostServiceImpl implements BoardPostService {
    private final BoardPostMapper boardPostMapper;

    @Autowired
    public BoardPostServiceImpl(BoardPostMapper boardPostMapper) {
        this.boardPostMapper = boardPostMapper;
    }

    @Override
    public List<BoardPostVO> getBoardList(int page) {
        return boardPostMapper.getBoardList(page);
    }

    @Override
    public int getTotalCount() {
        return boardPostMapper.getTotalCount();
    }

	@Override
	public List<BoardPostVO> getPagedPostsByKeyword(String keyword, int page) {
		  return boardPostMapper.getPagedPostsByKeyword(keyword, page);
	}

	@Override
	public int getTotalCountByKeyword(String keyword) {
		return boardPostMapper.getTotalCountByKeyword(keyword);
	}

	@Override
	public BoardPostVO getBoardDetail(int postId) {
		return boardPostMapper.selectPostById(postId);
	}

	@Override
	public int modifyBoartFixed(int postId) {
		return boardPostMapper.modifyBoartFixed(postId);
	}

	@Override
	public int removeBoradPost(int postId) {
		return boardPostMapper.deletePost(postId);
	}

	@Override
	public int createBoard(BoardPostVO boardPost) {
		return boardPostMapper.insertPost(boardPost);
	}

	@Override
	public int modifyBoard(BoardPostVO boardPost) {
		return boardPostMapper.updatePost(boardPost);
	}

	@Override
	public BoardPostVO findinfoChildPostByParentId(int postId) {
		return boardPostMapper.selectChildPostById(postId);
	}
}

