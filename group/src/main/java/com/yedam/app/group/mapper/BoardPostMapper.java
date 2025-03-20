package com.yedam.app.group.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yedam.app.group.service.BoardPostVO;

@Mapper
public interface BoardPostMapper {
    // 게시글 삽입
	int insertPost(BoardPostVO post);
//    
	// // 게시글 단건 조회
	BoardPostVO selectPostById(int postId);
//    
	// 전체 게시글 조회
//    List<BoardPostVO> selectAllPosts(int boardId);
//    
//     //게시글 수정
//    void updatePost(BoardPostVO post);
//    
    // 게시글 삭제
    int deletePost(int postId);

	// qna 5개씩

	// 일반 페이징된 게시글 목록 가져오기
	List<BoardPostVO> getBoardList(@Param("page") int page);

	// 전체 게시글 개수 조회
	int getTotalCount();

	// 검색어가 포함된 페이징된 게시글 목록 가져오기
	List<BoardPostVO> getPagedPostsByKeyword(@Param("keyword") String keyword, @Param("page") int page);

	// 검색어가 포함된 게시글 개수 조회
	int getTotalCountByKeyword(@Param("keyword") String keyword);
	
	int modifyBoartFixed(@Param("postId") int postId);

}
