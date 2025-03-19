package com.yedam.app.group.service;

import java.util.List;

public interface ModuleService {
    List<ModuleVO> getAllModules(); 
//    ModuleVO getModuleByNo(int moduleNo); 
    void addModule(ModuleVO module);
    void updateModule(ModuleVO module);
    void deleteModule(int moduleNo);
    void updateModuleBasic(int moduleNo);
    void updateModuleActive(int moduleNo);
}
