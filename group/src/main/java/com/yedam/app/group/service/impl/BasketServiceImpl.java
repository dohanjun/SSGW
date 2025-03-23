package com.yedam.app.group.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yedam.app.group.mapper.BasketMapper;
import com.yedam.app.group.service.BasketService;
import com.yedam.app.group.service.BasketVO;
import com.yedam.app.group.service.EmpVO;

@Service
public class BasketServiceImpl implements BasketService {
	 private final BasketMapper basketMapper;

	    public BasketServiceImpl(BasketMapper basketMapper) {
	        this.basketMapper = basketMapper;
	    }
	    
	    @Override
	    public void restoreSelectedPosts(List<Long> writingIds) {
	        for (Long id : writingIds) {
	            BasketVO basket = basketMapper.selectBasketByWritingId(id);

	            if (basket == null) continue;

	            // 복원 처리
	            switch (basket.getRepositoryType()) {
	                case "전체" -> basketMapper.restoreToTotalRepository(id);
	                case "부서" -> basketMapper.restoreToDepartmentRepository(id);
	                case "개인" -> basketMapper.restoreToIndividualRepository(id);
	            }

	            // 복원 후 휴지통에서 제거
	            basketMapper.deleteBasketByWritingId(id);
	        }
	    }

	    @Override
	    public void moveToBasket(List<Long> writingIds) {
	        for (Long id : writingIds) {
	        	String type = basketMapper.getRepositoryTypeByWritingId(id);
	        	basketMapper.insertToBasketWithType(id, type);  // type도 저장
	        }
	    }
	    
	    @Override
	    public List<BasketVO> getAllBasketPostsByEmp(EmpVO emp) {
	        return basketMapper.selectAllFromBasketBySuber(emp.getSuberNo());
	    }

	    @Override
	    public List<BasketVO> getBasketPostsByType(EmpVO emp, String repositoryType) {
	        Map<String, Object> params = new HashMap<>();
	        params.put("suberNo", emp.getSuberNo());
	        params.put("departmentNo", emp.getDepartmentNo());
	        params.put("employeeNo", emp.getEmployeeNo());
	        params.put("repositoryType", repositoryType);

	        return basketMapper.selectBasketByTypeFiltered(params);
	    }
	    
	    @Override
	    public List<BasketVO> getAllBasketPosts() {
	        return basketMapper.selectAllFromBasket();
	    }

	    @Override
	    public void permanentlyDeletePosts(List<Long> writingIds) {
	        for (Long id : writingIds) {
	            basketMapper.deleteFilesByWritingId(id);   // 파일 먼저 삭제
	            basketMapper.deletePostByWritingId(id);    // 게시글 삭제
	            basketMapper.deleteBasketByWritingId(id);  // 휴지통에서 삭제
	        }
	    }
}
