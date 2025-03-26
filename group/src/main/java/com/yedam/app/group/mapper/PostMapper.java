package com.yedam.app.group.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yedam.app.group.service.RepositoryPostVO;
import com.yedam.app.group.service.RepositoryVO;

@Mapper
public interface PostMapper {
	
	// 게시글 등록
    void insertPost(RepositoryPostVO postVO);

    // 전체 자료실 조회
    RepositoryVO getTotalRepository(int suberNo);

    // 부서 자료실 조회
    RepositoryVO getDepartmentRepository(int suberNo, int departmentNo);

    // 개인 자료실 조회
    RepositoryVO getIndividualRepository(int suberNo, int employeeNo);
    
    // 사원 정보 기반 자료실 자동 조회 (추가됨)
    RepositoryVO getRepositoryByUserInfo(Map<String, Object> params);
    
    // 자료글 상세 보기
    RepositoryPostVO getPostDetail(Long writingId);
    
    void updatePost(RepositoryPostVO postVO);
    
    void updateFixStatus(@Param("writingId") Long writingId, @Param("fix") char fix);
    
    // 전체 자료실 - 페이징 + 검색된 게시글 리스트 조회
    List<RepositoryPostVO> selectTotalRepositoryPostsPaged(
        @Param("suberNo") int suberNo,
        @Param("keyword") String keyword,
        @Param("offset") int offset,
        @Param("limit") int limit
    );

    // 전체 자료실 - 검색된 게시글 총 개수 조회
    
    int countTotalRepositoryPosts(@Param("suberNo") int suberNo, @Param("keyword") String keyword);
    
    int getDepartmentRepositoryPostCount(@Param("suberNo") int suberNo,
            @Param("departmentNo") int departmentNo,
            @Param("keyword") String keyword);

    List<RepositoryPostVO> getDepartmentRepositoryPostsPaged(@Param("suberNo") int suberNo,
                                	@Param("departmentNo") int departmentNo,
                                	@Param("keyword") String keyword,
                                	@Param("offset") int offset,
                                	@Param("limit") int limit);

    int getIndividualRepositoryPostCount(@Param("suberNo") int suberNo,
            	@Param("employeeNo") int employeeNo,
            	@Param("keyword") String keyword);

    List<RepositoryPostVO> getIndividualRepositoryPostsPaged(@Param("suberNo") int suberNo,
                                	@Param("employeeNo") int employeeNo,
                                	@Param("keyword") String keyword,
                                	@Param("offset") int offset,
                                	@Param("limit") int limit);
    
    List<RepositoryPostVO> selectFixedPosts(Map<String, Object> paramMap);
    
    List<RepositoryPostVO> selectDepartmentFixedPosts(Map<String, Object> paramMap);
    
    List<RepositoryPostVO> selectIndividualFixedPosts(Map<String, Object> paramMap);
}
