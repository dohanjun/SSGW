package com.yedam.app.group.service.impl;

import java.util.List;

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
    public int insertPost(RepositoryPostVO postVO) {
        return postMapper.insertPost(postVO);
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
    public List<RepositoryPostVO> getTotalRepositoryPosts(int fileRepositoryId) {
        return postMapper.getTotalRepositoryPosts(fileRepositoryId);
    }

}
