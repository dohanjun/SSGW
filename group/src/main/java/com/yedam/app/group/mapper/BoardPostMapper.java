package com.yedam.app.group.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yedam.app.group.service.BoardPostVO;

@Mapper
public interface BoardPostMapper {
    // 게시글 삽입
    int insertPost(BoardPostVO post);

    // 게시글 단건 조회
    BoardPostVO selectPostById(int postId);

    // 게시글 수정
    int updatePost(BoardPostVO post);

    // 게시글 삭제
    int deletePost(int postId);

    int deleteAllPost(int postId);
    
    // 게시글 고정 여부 수정
    int modifyBoartFixed(@Param("postId") int postId);

    // 자식 답글 조회
    BoardPostVO selectChildPostById(int postId);

    // 🔄 통합 필터 기반 게시글 목록 조회 (전체, 검색, 내 글 포함)
    List<BoardPostVO> selectBoardListWithFilter(@Param("employeeNo") Integer employeeNo,
                                                @Param("keyword") String keyword,
                                                @Param("offset") int offset);

    // 🔄 통합 필터 기반 게시글 개수 조회
    int selectBoardCountWithFilter(@Param("employeeNo") Integer employeeNo,
                                   @Param("keyword") String keyword);

    // ✅ 기존 메서드들도 유지 (원하면 삭제 가능)
    List<BoardPostVO> getBoardList(@Param("page") int page);

    int getTotalCount();

    List<BoardPostVO> getPagedPostsByKeyword(@Param("keyword") String keyword,
                                             @Param("page") int page);

    int getTotalCountByKeyword(@Param("keyword") String keyword);
}
