package com.yedam.app.group.service;

import java.util.List;

public interface BasketService {
	
	// 전체 자료실 - 관리자
    int countAllBasketPosts(String keyword, int suberNo);
    List<BasketVO> getAllBasketPostsPaged(String keyword, int suberNo, int offset, int limit);

    // 전체 자료실 - 일반 직원
    int countOwnTotalBasketPosts(String keyword, int employeeNo);
    List<BasketVO> getOwnTotalBasketPostsPaged(String keyword, int employeeNo, int offset, int limit);

    // 부서 자료실
    int countDepartmentBasketPosts(String keyword, int suberNo, int departmentNo, int employeeNo, Integer manager);
    List<BasketVO> getDepartmentBasketPostsPaged(String keyword, int suberNo, int departmentNo, int employeeNo, Integer manager, int offset, int limit);

    // 개인 자료실
    int countIndividualBasketPosts(String keyword, int employeeNo);
    List<BasketVO> getIndividualBasketPostsPaged(String keyword, int employeeNo, int offset, int limit);
	
	void moveToBasket(List<Long> writingIds, String repositoryType);
	
    List<BasketVO> getAllBasketPosts();
    
    List<BasketVO> getAllBasketPostsByEmp(EmpVO emp);
    
    List<BasketVO> getBasketPostsByType(EmpVO emp, String repositoryType);
    
    List<BasketVO> getDepartmentBasketFiltered(EmpVO emp);
    
    List<BasketVO> getIndividualBasket(EmpVO emp);
    
    void restoreSelectedPosts(List<Long> writingIds);

    void permanentlyDeletePosts(List<Long> writingIds);
    
    List<BasketVO> getOwnTotalBasketPosts(EmpVO emp);
    
    BasketVO getBasketPostDetail(Long writingId);
    
}
