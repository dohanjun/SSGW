package com.yedam.app.group.service;

import java.util.List;

public interface BasketService {
	
	void moveToBasket(List<Long> writingIds);
	
    List<BasketVO> getAllBasketPosts();
    
    List<BasketVO> getAllBasketPostsByEmp(EmpVO emp);
    
    List<BasketVO> getBasketPostsByType(EmpVO emp, String repositoryType);
    
    void restoreSelectedPosts(List<Long> writingIds);

    void permanentlyDeletePosts(List<Long> writingIds);
}
