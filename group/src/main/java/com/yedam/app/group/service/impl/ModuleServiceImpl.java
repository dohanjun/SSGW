package com.yedam.app.group.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yedam.app.group.mapper.ModuleMapper;
import com.yedam.app.group.service.ModuleService;
import com.yedam.app.group.service.ModuleVO;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ModuleServiceImpl implements ModuleService{
	
	private final ModuleMapper moduleMapper;
	  
    @Override
    public List<ModuleVO> getAllModules() {
        return moduleMapper.getAllModules();
    }

}
