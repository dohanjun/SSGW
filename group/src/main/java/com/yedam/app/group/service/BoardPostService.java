package com.yedam.app.group.service;

import java.util.List;

public interface BoardPostService {
	List<BoardPostVO> getBoardListWithOptionalFilter(Integer employeeNo, String keyword, int offset);
	int getBoardCountWithOptionalFilter(Integer employeeNo, String keyword);

    
	BoardPostVO getBoardDetail(int postId);
	
	int modifyBoartFixed(int postId);
	
	int removeBoradPost(int postId);
	
	int modifyBoard(BoardPostVO boardPost);
	
	int createBoard(BoardPostVO boardPost);
	
	BoardPostVO findinfoChildPostByParentId(int postId);

}