package com.yedam.app.group.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.yedam.app.group.service.SubscriptionDetailVO;


@Mapper
public interface ModuleDetailMapper {
    void insertModules(SubscriptionDetailVO modules);
}
