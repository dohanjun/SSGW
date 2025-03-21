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
    
    // 전체 자료실 게시글 조회
    @Override
    public List<RepositoryPostVO> getTotalRepositoryPosts(int suberNo) {
        Map<String, Integer> params = new HashMap<>();
        params.put("suberNo", suberNo);
        return postMapper.getTotalRepositoryPosts(params);
    }

    // 부서 자료실 게시글 조회
    @Override
    public List<RepositoryPostVO> getDepartmentRepositoryPosts(int suberNo, int departmentNo) {
        Map<String, Integer> params = new HashMap<>();
        params.put("suberNo", suberNo);
        params.put("departmentNo", departmentNo);
        return postMapper.getDepartmentRepositoryPosts(params);
    }
    
    @Override
    public RepositoryPostVO getPostDetail(Long writingId) {
        return postMapper.getPostDetail(writingId);
    }

    // 개인 자료실 게시글 조회
    @Override
    public List<RepositoryPostVO> getIndividualRepositoryPosts(int suberNo, int employeeNo) {
        Map<String, Integer> params = new HashMap<>();
        params.put("suberNo", suberNo);
        params.put("employeeNo", employeeNo);
        return postMapper.getIndividualRepositoryPosts(params);
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
}
