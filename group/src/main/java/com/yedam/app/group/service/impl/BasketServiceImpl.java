package com.yedam.app.group.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yedam.app.group.mapper.BasketMapper;
import com.yedam.app.group.service.BasketService;
import com.yedam.app.group.service.BasketVO;
import com.yedam.app.group.service.EmpVO;
import com.yedam.app.group.service.FileService;

@Service
public class BasketServiceImpl implements BasketService {
	 private final BasketMapper basketMapper;
	 private final FileService fileService;

	    public BasketServiceImpl(BasketMapper basketMapper, FileService fileService) {
	        this.basketMapper = basketMapper;
	        this.fileService = fileService;
	    }
	    
	    @Override
	    public void restoreSelectedPosts(List<Long> writingIds) {
	    	for (Long id : writingIds) {
	            BasketVO basket = basketMapper.selectBasketByWritingId(id);
	            if (basket == null) continue;

	            // 파일 복원 (백업 → 업로드 폴더로 이동)
	            try {
	                fileService.restoreFilesByWritingId(id);  // ← 이거 추가!
	            } catch (Exception e) {
	            }

	            // 휴지통에서 제거
	            basketMapper.deleteBasketByWritingId(id);
	        }
	    }

	    @Override
	    public void moveToBasket(List<Long> writingIds, String repositoryType) {
	    	if (writingIds == null || writingIds.isEmpty()) return;

	        for (Long id : writingIds) {
	            if (id == null) {
	                continue;
	            }

	            String type = basketMapper.getRepositoryTypeByWritingId(id);
	            if (type == null) {
	                continue;
	            }

	            // 파일 백업 처리 (upload → backup 폴더로 이동)
	            try {
	                fileService.backupFilesByWritingId(id);
	            } catch (Exception e) {
	            }

	            // 휴지통으로 등록
	            basketMapper.insertToBasketWithType(id, type);
	        }
	    }
	    
	    @Override
	    public List<BasketVO> getAllBasketPostsByEmp(EmpVO emp) {
	    	boolean isAdmin = (emp.getRightsId() != null && emp.getRightsId() == 3) ||
                    (emp.getRightsLevel() != null && emp.getRightsLevel() == 5);

	    	if (isAdmin) {
	    		return basketMapper.selectAllFromBasketBySuber(emp.getSuberNo());
	    	} else {
	    		return basketMapper.selectOwnTotalBasketPosts(emp.getSuberNo(), emp.getEmployeeNo());
	    	}
	    }

	    @Override
	    public List<BasketVO> getBasketPostsByType(EmpVO emp, String repositoryType) {
	        Map<String, Object> params = new HashMap<>();
	        params.put("suberNo", emp.getSuberNo());
	        params.put("departmentNo", emp.getDepartmentNo());
	        params.put("employeeNo", emp.getEmployeeNo());
	        params.put("repositoryType", repositoryType);

	        if ("부서".equals(repositoryType)) {
	            // 부서 자료실 휴지통 접근: 작성자 또는 부서장만
	            return basketMapper.selectDepartmentBasketFiltered(params);
	        } else {
	            // 그 외는 기존 로직 유지
	            return basketMapper.selectBasketByTypeFiltered(params);
	        }
	    }
	    
	    @Override
	    public List<BasketVO> getDepartmentBasketFiltered(EmpVO emp) {
	        Map<String, Object> params = new HashMap<>();
	        params.put("departmentNo", emp.getDepartmentNo());
	        return basketMapper.selectDepartmentBasketFiltered(params);
	    }

	    @Override
	    public List<BasketVO> getIndividualBasket(EmpVO emp) {
	        Map<String, Object> params = new HashMap<>();
	        params.put("employeeNo", emp.getEmployeeNo());
	        return basketMapper.selectIndividualBasket(params);
	    }
	    
	    @Override
	    public List<BasketVO> getAllBasketPosts() {
	        return basketMapper.selectAllFromBasket();
	    }

	    @Override
	    public void permanentlyDeletePosts(List<Long> writingIds) {
	    	for (Long id : writingIds) {
	            if (id == null) continue;

	            // 1. 먼저 파일 ID 조회 (repository_file → file_id)
	            List<Long> fileIds = basketMapper.getFileIdsByWritingId(id);

	            // 2. 자식 테이블 먼저 삭제 (예: download 로그 등)
	            for (Long fileId : fileIds) {
	                basketMapper.deleteDownloadLogsByFileId(fileId);
	            }

	            // 3. repository_file 삭제
	            basketMapper.deleteFilesByWritingId(id);

	            // 4. repository_post 삭제 (basket도 자동으로 CASCADE 삭제됨)
	            int deleted = basketMapper.deletePostByWritingId(id);
	        }
	    }
	    
	    @Override
	    public List<BasketVO> getOwnTotalBasketPosts(EmpVO emp) {
	        return basketMapper.selectOwnTotalBasketPosts(emp.getSuberNo(), emp.getEmployeeNo());
	    }
	    
	    @Override
	    public BasketVO getBasketPostDetail(Long writingId) {
	        return basketMapper.getBasketDetailById(writingId);
	    }
	    
	    // 전체 자료실 - 관리자
	    @Override
	    public int countAllBasketPosts(String keyword, int suberNo) {
	        Map<String, Object> map = new HashMap<>();
	        map.put("keyword", keyword);
	        map.put("suberNo", suberNo);
	        return basketMapper.countAllBasketPosts(map);
	    }
	    
	    @Override
	    public List<BasketVO> getAllBasketPostsPaged(String keyword, int suberNo, int offset, int limit) {
	        Map<String, Object> map = new HashMap<>();
	        map.put("keyword", keyword);
	        map.put("suberNo", suberNo);
	        map.put("offset", offset);
	        map.put("limit", limit);
	        return basketMapper.selectAllBasketPostsPaged(map);
	    }
	    
	    // 전체 자료실 - 일반 직원
	    @Override
	    public int countOwnTotalBasketPosts(String keyword, int employeeNo) {
	        Map<String, Object> map = new HashMap<>();
	        map.put("keyword", keyword);
	        map.put("employeeNo", employeeNo);
	        return basketMapper.countOwnTotalBasketPosts(map);
	    }
	    
	    @Override
	    public List<BasketVO> getOwnTotalBasketPostsPaged(String keyword, int employeeNo, int offset, int limit) {
	        Map<String, Object> map = new HashMap<>();
	        map.put("keyword", keyword);
	        map.put("employeeNo", employeeNo);
	        map.put("offset", offset);
	        map.put("limit", limit);
	        return basketMapper.selectOwnTotalBasketPostsPaged(map);
	    }
	    
	    // 부서 자료실
	    @Override
	    public int countDepartmentBasketPosts(String keyword, int suberNo, int departmentNo, int employeeNo, Integer manager) {
	        Map<String, Object> map = new HashMap<>();
	        map.put("keyword", keyword);
	        map.put("suberNo", suberNo);
	        map.put("departmentNo", departmentNo);
	        map.put("employeeNo", employeeNo);
	        map.put("manager", manager);
	        return basketMapper.countDepartmentBasketPosts(map);
	    }
	    
	    @Override
	    public List<BasketVO> getDepartmentBasketPostsPaged(String keyword, int suberNo, int departmentNo, int employeeNo, Integer manager, int offset, int limit) {
	        Map<String, Object> map = new HashMap<>();
	        map.put("keyword", keyword);
	        map.put("suberNo", suberNo);
	        map.put("departmentNo", departmentNo);
	        map.put("employeeNo", employeeNo);
	        map.put("manager", manager);
	        map.put("offset", offset);
	        map.put("limit", limit);
	        return basketMapper.selectDepartmentBasketPostsPaged(map);
	    }
	    
	    // 개인 자료실
	    @Override
	    public int countIndividualBasketPosts(String keyword, int employeeNo) {
	        Map<String, Object> map = new HashMap<>();
	        map.put("keyword", keyword);
	        map.put("employeeNo", employeeNo);
	        return basketMapper.countIndividualBasketPosts(map);
	    }
	    
	    @Override
	    public List<BasketVO> getIndividualBasketPostsPaged(String keyword, int employeeNo, int offset, int limit) {
	        Map<String, Object> map = new HashMap<>();
	        map.put("keyword", keyword);
	        map.put("employeeNo", employeeNo);
	        map.put("offset", offset);
	        map.put("limit", limit);
	        return basketMapper.selectIndividualBasketPostsPaged(map);
	    }

}
