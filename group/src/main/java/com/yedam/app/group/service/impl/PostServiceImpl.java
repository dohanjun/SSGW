package com.yedam.app.group.service.impl;

import org.springframework.stereotype.Service;

import com.yedam.app.group.mapper.PostMapper;
import com.yedam.app.group.service.PostService;
import com.yedam.app.group.service.RepositoryPostVO;

@Service
public class PostServiceImpl implements PostService {
	
	private final PostMapper postMapper;

    public PostServiceImpl(PostMapper postMapper) {
        this.postMapper = postMapper;
    }

    @Override
    public int insertPost(RepositoryPostVO postVO) {
        return postMapper.insertPost(postVO);
    }

}
