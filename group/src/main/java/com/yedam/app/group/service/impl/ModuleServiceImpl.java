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

	@Override
	public void addModule(ModuleVO module) {
		moduleMapper.insertModule(module);
	}

	@Override
	public void updateModule(ModuleVO module) {
		moduleMapper.updateModule(module);
		
	}

	@Override
	public void deleteModule(int moduleNo) {
		moduleMapper.deleteModule(moduleNo);
	}

	@Override
	public void updateModuleBasic(int moduleNo) {
		moduleMapper.updateBasic(moduleNo);
	}

	@Override
	public void updateModuleActive(int moduleNo) {
		moduleMapper.updateActive(moduleNo);
	}
	@Override
	public List<Integer> findSubscribedModuleNos(int suberNo, List<Integer> moduleNos) {
	    return moduleMapper.findSubscribedModuleNos(suberNo, moduleNos);
	}
}
