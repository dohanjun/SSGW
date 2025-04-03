package com.yedam.app.group.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yedam.app.group.mapper.PostMapper;
import com.yedam.app.group.service.PostService;
import com.yedam.app.group.service.RepositoryPostVO;
import com.yedam.app.group.service.RepositoryVO;

@Service
public class PostServiceImpl implements PostService {
	
	private final PostMapper postMapper;
	
	@Autowired
    public PostServiceImpl(PostMapper postMapper) {
        this.postMapper = postMapper;
    }

    @Override
    public Long insertPost(RepositoryPostVO postVO) {
    	postMapper.insertPost(postVO);

        if (postVO.getWritingId() == null) {
            throw new IllegalStateException("WRITING_ID가 설정되지 않았습니다.");
        }

        return postVO.getWritingId();
    }
    
    @Override
    public RepositoryPostVO getPostDetail(Long writingId) {
        return postMapper.getPostDetail(writingId);
    }
    
    @Override
    public RepositoryVO getTotalRepository(int suberNo) {
        return postMapper.getTotalRepository(suberNo);
    }

    @Override
    public RepositoryVO getDepartmentRepository(int suberNo, int departmentNo) {
        return postMapper.getDepartmentRepository(suberNo, departmentNo);
    }

    @Override
    public RepositoryVO getIndividualRepository(int suberNo, int employeeNo) {
        return postMapper.getIndividualRepository(suberNo, employeeNo);
    }
    
    @Override
    public RepositoryVO getRepositoryByUserInfo(int suberNo, int departmentNo, int employeeNo) {
        // 우선순위: 개인 → 부서 → 전체
        RepositoryVO personalRepo = postMapper.getIndividualRepository(suberNo, employeeNo);
        if (personalRepo != null) {
            return personalRepo;
        }

        RepositoryVO departmentRepo = postMapper.getDepartmentRepository(suberNo, departmentNo);
        if (departmentRepo != null) {
            return departmentRepo;
        }

        RepositoryVO totalRepo = postMapper.getTotalRepository(suberNo);
        if (totalRepo != null) {
            return totalRepo;
        }

        return null; // 셋 다 없을 경우
    }
    
    @Override
    public List<RepositoryPostVO> getFixedPosts(int suberNo, String keyword) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("suberNo", suberNo);
        paramMap.put("keyword", keyword);
        return postMapper.selectFixedPosts(paramMap);
    }
    
    @Override
    public List<RepositoryPostVO> getDepartmentFixedPosts(int suberNo, int departmentNo, String keyword) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("suberNo", suberNo);
        paramMap.put("departmentNo", departmentNo);
        paramMap.put("keyword", keyword);
        return postMapper.selectDepartmentFixedPosts(paramMap);
    }
    
    @Override
    public void updatePost(RepositoryPostVO postVO) {
        postMapper.updatePost(postVO);
    }
    
    @Override
    public void updateFixStatus(Long writingId, char fix) {
        postMapper.updateFixStatus(writingId, fix);
    }
    
    @Override
    public List<RepositoryPostVO> getTotalRepositoryPostsPaged(int suberNo, String keyword, int start, int end) {
        return postMapper.selectTotalRepositoryPostsPaged(suberNo, keyword, start, end);
    }

    @Override
    public int getTotalRepositoryPostCount(int suberNo, String keyword) {
        return postMapper.countTotalRepositoryPosts(suberNo, keyword);
    }
    
    @Override
    public int getDepartmentRepositoryPostCount(int suberNo, int departmentNo, String keyword) {
        return postMapper.getDepartmentRepositoryPostCount(suberNo, departmentNo, keyword);
    }

    @Override
    public List<RepositoryPostVO> getDepartmentRepositoryPostsPaged(int suberNo, int departmentNo, String keyword, int offset, int limit) {
        return postMapper.getDepartmentRepositoryPostsPaged(suberNo, departmentNo, keyword, offset, limit);
    }

    @Override
    public int getIndividualRepositoryPostCount(int suberNo, int employeeNo, String keyword) {
        return postMapper.getIndividualRepositoryPostCount(suberNo, employeeNo, keyword);
    }

    @Override
    public List<RepositoryPostVO> getIndividualRepositoryPostsPaged(int suberNo, int employeeNo, String keyword, int offset, int limit) {
        return postMapper.getIndividualRepositoryPostsPaged(suberNo, employeeNo, keyword, offset, limit);
    }
    
    @Override
    public int countFixedPosts(Integer suberNo, String repositoryType) {
        return postMapper.countFixedPosts(suberNo, repositoryType);
    }
}
