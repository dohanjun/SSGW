package com.yedam.app.group.mapper;

import java.util.List;

import com.yedam.app.group.service.BookVO;
import com.yedam.app.group.service.PageListVO;

public interface AddressBookMapper {

	//메일전체조회
		public List<BookVO> BookfindAll(PageListVO vo);
}
