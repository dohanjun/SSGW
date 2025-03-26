package com.yedam.app.group.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yedam.app.group.mapper.ModuleDetailMapper;
import com.yedam.app.group.service.ModuleDetailService;
import com.yedam.app.group.service.SubscriptionDetailVO;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ModuleDetailServiceImpl implements ModuleDetailService {
    @Autowired
    private ModuleDetailMapper moduleDetailMapper;

    @Override
    public List<SubscriptionDetailVO> saveModuleDetail(List<SubscriptionDetailVO> list) {
        for (SubscriptionDetailVO vo : list) {
            moduleDetailMapper.insertModules(vo);
            log.debug("✅ 저장된 SUB_DETAILS_NO (MyBatis 반영): " + vo.getSubDetailsNo()); // 🔹 값 확인
        }
        return list;
    }

}
