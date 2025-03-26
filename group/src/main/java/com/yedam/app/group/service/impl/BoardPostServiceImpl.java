package com.yedam.app.group.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.yedam.app.group.mapper.BoardPostMapper;
import com.yedam.app.group.service.BoardPostService;
import com.yedam.app.group.service.BoardPostVO;

import java.util.List;

@Service
public class BoardPostServiceImpl implements BoardPostService {

	private final BoardPostMapper boardPostMapper;

	@Autowired
	public BoardPostServiceImpl(BoardPostMapper boardPostMapper) {
		this.boardPostMapper = boardPostMapper;
	}

	// 내 글 + 조건 검색 포함 공통 메서드
	@Override
	public List<BoardPostVO> getBoardListWithOptionalFilter(Integer employeeNo, String keyword, int offset) {
		return boardPostMapper.selectBoardListWithFilter(employeeNo, keyword, offset);
	}

	@Override
	public int getBoardCountWithOptionalFilter(Integer employeeNo, String keyword) {
		return boardPostMapper.selectBoardCountWithFilter(employeeNo, keyword);
	}

	// 게시글 상세
	@Override
	public BoardPostVO getBoardDetail(int postId) {
		return boardPostMapper.selectPostById(postId);
	}

	// 고정 상태 수정
	@Override
	public int modifyBoartFixed(int postId) {
		return boardPostMapper.modifyBoartFixed(postId);
	}

	// 게시글 삭제
	@Override
	public int removeBoradPost(int postId) {
		return boardPostMapper.deletePost(postId);
	}

	// 게시글 등록
	@Override
	public int createBoard(BoardPostVO boardPost) {
		return boardPostMapper.insertPost(boardPost);
	}

	// 게시글 수정
	@Override
	public int modifyBoard(BoardPostVO boardPost) {
		return boardPostMapper.updatePost(boardPost);
	}

	// 자식 답글 조회
	@Override
	public BoardPostVO findinfoChildPostByParentId(int postId) {
		return boardPostMapper.selectChildPostById(postId);
	}

	@Override
	public int removeAllBoradPost(int postId) {
		return boardPostMapper.deleteAllPost(postId);
	}

}