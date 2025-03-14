package com.yedam.app.group.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.yedam.app.group.service.ModuleVO;

@Mapper
public interface ModuleMapper {
	List<ModuleVO> getAllModules();
}
