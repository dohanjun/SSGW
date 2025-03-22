package com.yedam.app.group.service;

import java.util.List;

public interface AddressBookService {

	//메일전체조회
		public List<BookVO> BookselectAll(PageListVO vo);
		
	//메일 페이지네이션
		public int pageGetCount(PageListVO pagelistVO);
}
