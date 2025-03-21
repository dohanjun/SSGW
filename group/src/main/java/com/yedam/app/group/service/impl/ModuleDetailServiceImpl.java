package com.yedam.app.group.service.impl;

import com.yedam.app.group.mapper.ModuleDetailMapper;
import com.yedam.app.group.service.ModuleDetailService;
import com.yedam.app.group.service.SubscriptionDetailVO;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Service
public class ModuleDetailServiceImpl implements ModuleDetailService {
    @Autowired
    private ModuleDetailMapper moduleDetailMapper;

    @Override
    public List<SubscriptionDetailVO> saveModuleDetail(List<SubscriptionDetailVO> list) {
        for (SubscriptionDetailVO vo : list) {
            moduleDetailMapper.insertModules(vo);
            System.out.println("✅ 저장된 SUB_DETAILS_NO (MyBatis 반영): " + vo.getSubDetailsNo()); // 🔹 값 확인
        }
        return list;
    }

}
