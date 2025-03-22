package com.yedam.app.group.service;

public interface RepositoryService {
	RepositoryVO getTotalRepository(int suberNo);  // 회사 자료실 조회
	
    RepositoryVO getDepartmentRepository(int suberNo, int departmentNo);  // 부서 자료실 조회
    
    RepositoryVO getIndividualRepository(int suberNo, int employeeNo);  // 개인 자료실 조회 
    
    RepositoryPostVO getPostDetail(Long writingId);
}
