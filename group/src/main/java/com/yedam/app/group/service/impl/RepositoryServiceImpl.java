package com.yedam.app.group.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yedam.app.group.mapper.PostMapper;
import com.yedam.app.group.mapper.RepositoryMapper;
import com.yedam.app.group.service.RepositoryPostVO;
import com.yedam.app.group.service.RepositoryService;
import com.yedam.app.group.service.RepositoryVO;

@Service
public class RepositoryServiceImpl implements RepositoryService {
	
	private final RepositoryMapper repositoryMapper;
	
	private final PostMapper postMapper;

    @Autowired
    public RepositoryServiceImpl(RepositoryMapper repositoryMapper, PostMapper postMapper) {
        this.repositoryMapper = repositoryMapper;
        this.postMapper = postMapper;
    }

    @Override
    public RepositoryVO getTotalRepository(int suberNo) {
        return repositoryMapper.findTotalRepository(suberNo);
    }

    @Override
    public RepositoryVO getDepartmentRepository(int suberNo, int departmentNo) {
        return repositoryMapper.findDepartmentRepository(suberNo, departmentNo);
    }

    @Override
    public RepositoryVO getIndividualRepository(int suberNo, int employeeNo) {
        return repositoryMapper.findIndividualRepository(suberNo, employeeNo);
    }
    
    @Override
    public RepositoryPostVO getPostDetail(Long writingId) {
        return postMapper.getPostDetail(writingId);
    }
}
