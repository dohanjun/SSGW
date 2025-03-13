package com.yedam.app.group.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yedam.app.group.mapper.GroupMapper;
import com.yedam.app.group.service.GroupService;
import com.yedam.app.group.service.GroupVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class GroupServiceimpl implements GroupService{
	
	private final GroupMapper groupMapper;
	
	
	@Override
	public List<GroupVO> findAllList() {
		return groupMapper.selectAllList();
	}
	
}
