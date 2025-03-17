package com.yedam.app.group.mapper;

import com.yedam.app.group.service.ManagerVO;

public interface ManagerMapper {
	ManagerVO getManagerById(String managerId);
}