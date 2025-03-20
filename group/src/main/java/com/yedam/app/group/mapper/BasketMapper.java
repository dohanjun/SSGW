package com.yedam.app.group.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.yedam.app.group.service.BasketVO;

@Mapper
public interface BasketMapper {
	// 휴지통에 파일 등록 (파일 이동)
    int insertToBasket(BasketVO basketVO);
}
