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
    
    // 전체 자료실 게시글 조회
    List<RepositoryPostVO> getTotalRepositoryPosts(int suberNo);

    // 부서 자료실 게시글 조회
    List<RepositoryPostVO> getDepartmentRepositoryPosts(int suberNo, int departmentNo);

    // 개인 자료실 게시글 조회
    List<RepositoryPostVO> getIndividualRepositoryPosts(int suberNo, int employeeNo);
}
