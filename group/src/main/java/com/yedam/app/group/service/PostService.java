package com.yedam.app.group.service;

import java.util.List;

public interface PostService {
	// 게시글 등록
    int insertPost(RepositoryPostVO postVO);
    
    // 전체 자료실 ID 조회
    RepositoryVO getTotalRepository(int suberNo);
    
    // 부서 자료실 ID 조회
    RepositoryVO getDepartmentRepository(int suberNo, int departmentNo);
    
    // 개인 자료실 ID 조회
    RepositoryVO getIndividualRepository(int suberNo, int employeeNo);
    
    List<RepositoryPostVO> getTotalRepositoryPosts(int fileRepositoryId);
}
