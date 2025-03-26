package com.yedam.app.group.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yedam.app.group.service.BoardAttachmentVO;
import com.yedam.app.group.service.BoardPostVO;
import com.yedam.app.group.service.BoardVO;

@Mapper
public interface BoardMapper {
	
	List<BoardVO> selectBoardsByType(@Param("boardType") String boardType, @Param("suberNo") int suberNo, @Param("departmentNo") Integer departmentNo,
		    @Param("employeeNo") Integer employeeNo
		);
	
	int countNoticeBoardPosts(@Param("suberNo") int suberNo, @Param("keyword") String keyword);
	
	List<BoardVO> selectNoticeBoardPostsPaged(@Param("suberNo") int suberNo, @Param("keyword") String keyword,
	                                          @Param("offset") int offset, @Param("limit") int limit);
	
	int countDepartmentBoardPosts(@Param("suberNo") int suberNo, @Param("departmentNo") Integer departmentNo, @Param("keyword") String keyword);
	
	List<BoardVO> selectDepartmentBoardPostsPaged(@Param("suberNo") int suberNo, @Param("departmentNo") Integer departmentNo,
	                                              @Param("keyword") String keyword, @Param("offset") int offset, @Param("limit") int limit);

	int countFreeBoardPosts(@Param("suberNo") int suberNo, @Param("keyword") String keyword);
	
	List<BoardVO> selectFreeBoardPostsPaged(@Param("suberNo") int suberNo, @Param("keyword") String keyword,
											@Param("offset") int offset, @Param("limit") int limit);
	
	Integer getBoardIdByType(@Param("type") String type,
            @Param("suberNo") int suberNo,
            @Param("departmentNo") Integer departmentNo);

	void insertPost(BoardPostVO postVO);

	void insertAttachment(BoardAttachmentVO attach);

}
