package com.yedam.app.group.service;

import java.util.List;

public interface BasketService {
	
	void moveToBasket(List<Long> writingIds);
	
    List<BasketVO> getAllBasketPosts();
    
    List<BasketVO> getAllBasketPostsByEmp(EmpVO emp);
    
    List<BasketVO> getBasketPostsByType(EmpVO emp, String repositoryType);
    
    List<BasketVO> getDepartmentBasketFiltered(EmpVO emp);
    
    List<BasketVO> getIndividualBasket(EmpVO emp);
    
    void restoreSelectedPosts(List<Long> writingIds);

    void permanentlyDeletePosts(List<Long> writingIds);
    
    List<BasketVO> getOwnTotalBasketPosts(EmpVO emp);
}
