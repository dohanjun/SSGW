package com.yedam.app.group.service;

import java.util.List;
import java.util.Map;

public interface PostService {
	// 게시글 등록
    Long insertPost(RepositoryPostVO postVO);
    
    // 전체 자료실 ID 조회
    RepositoryVO getTotalRepository(int suberNo);
    
    // 부서 자료실 ID 조회
    RepositoryVO getDepartmentRepository(int suberNo, int departmentNo);
    
    // 개인 자료실 ID 조회
    RepositoryVO getIndividualRepository(int suberNo, int employeeNo);
    
    // 사원 정보 기반으로 자료실 자동 판별
    RepositoryVO getRepositoryByUserInfo(int suberNo, int departmentNo, int employeeNo);
    
    // 단건 조회
    RepositoryPostVO getPostDetail(Long writingId);
    
    void updatePost(RepositoryPostVO postVO);
    
    void updateFixStatus(Long writingId, char fix);
    
    List<RepositoryPostVO> getFixedPosts(int suberNo, String keyword);
    
    // 페이징된 게시글 목록 조회 (전체 자료실용)
    List<RepositoryPostVO> getTotalRepositoryPostsPaged(int suberNo, String keyword, int start, int end);

    // 전체 게시글 수 조회 (검색어 포함)
    int getTotalRepositoryPostCount(int suberNo, String keyword);
    
    // 부서 자료실 페이징
    int getDepartmentRepositoryPostCount(int suberNo, int departmentNo, String keyword);
    
    List<RepositoryPostVO> getDepartmentRepositoryPostsPaged(int suberNo, int departmentNo, String keyword, int offset, int limit);

    // 개인 자료실 페이징
    int getIndividualRepositoryPostCount(int suberNo, int employeeNo, String keyword);
    
    List<RepositoryPostVO> getIndividualRepositoryPostsPaged(int suberNo, int employeeNo, String keyword, int offset, int limit);
    
    List<RepositoryPostVO> getDepartmentFixedPosts(int suberNo, int departmentNo, String keyword);

}
