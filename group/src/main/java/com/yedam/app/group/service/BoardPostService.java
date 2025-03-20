package com.yedam.app.group.service;

import java.util.List;

public interface BoardPostService {
    List<BoardPostVO> getBoardList(int page);
    int getTotalCount();
    
    List<BoardPostVO> getPagedPostsByKeyword(String keyword, int page);
    int getTotalCountByKeyword(String keyword);
    
	BoardPostVO getBoardDetail(int postId);
	int modifyBoartFixed(int postId);
	int removeBoradPost(int postId);
	
	int createBoard(BoardPostVO boardPost);
}