package com.yedam.app.group.mapper;

import java.util.List;

import com.yedam.app.group.service.AddressBookVO;
import com.yedam.app.group.service.MailVO;
import com.yedam.app.group.service.PageListVO;

public interface AddressBookMapper {

	//주소록검색기능
	public PageListVO getPageList(PageListVO vo);
	
	//부서 주소록전체조회
	public List<AddressBookVO> AddressBookFindAll(PageListVO vo);
	
	//개인 주소록전체조회
	public List<AddressBookVO> MyAddressBookFindAll(PageListVO vo);
	
	//주소록단건조회
	public AddressBookVO AddressBookFindInfo(int addressBookId);
	
	//나의 주소록 단건조회
	public AddressBookVO MyAddressBookFindInfo(int addressBookId);
	
	//주소록등록
	public int AddressBookCreate(AddressBookVO addressBookVO);
		
	//주소록수정
	public int AddressBookModify(AddressBookVO addressBookVO);

	//주소록삭제
	public int AddressBookRemove(int AddressBookId);
	
	//주소록 페이지네이션
	public int getCount(PageListVO pagelistVO);
	
	//주소록 페이지네이션
	public int getCounts(PageListVO pagelistVO);
}
