package com.yedam.app.group.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.yedam.app.group.service.ModuleVO;

@Mapper
public interface ModuleMapper {
	List<ModuleVO> getAllModules();
	int insertModule(ModuleVO moduleVO);
	int updateModule(ModuleVO moduleVO);
	int deleteModule(int moduleNo);
	int updateBasic(int moduleNo);
	int updateActive(int moduleNo);
}
