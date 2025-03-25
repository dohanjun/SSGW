package com.yedam.app.group.service;

import java.util.List;
import java.util.Map;

public interface AddressBookService {

	//주소록검색기능
	public PageListVO getPageList(PageListVO vo);
	
	//주소록전체조회
	public List<AddressBookVO> AddressBookSelectAll(PageListVO vo);
	
	//주소록단건조회
	public AddressBookVO AddressBookSelectInfo(AddressBookVO addressBookVO);
	
	//주소록메일단건조회
	public AddressBookVO MyAddressBookSelectInfo(AddressBookVO addressBookVO);
	
	//주소록등록
	public int AddressBookInsert(AddressBookVO addressBookVO);
	
	//주소록수정
	public Map<String, Object> AddressBookUpdate(AddressBookVO addressBookVO);

	//주소록삭제
	public Map<String, Object> AddressBookDelete(int AddressBookId);

	//주소록 페이지네이션
	public int pageGetCount(PageListVO vo);

}
