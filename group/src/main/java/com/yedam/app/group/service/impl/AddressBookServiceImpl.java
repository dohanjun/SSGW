package com.yedam.app.group.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.yedam.app.group.mapper.AddressBookMapper;
import com.yedam.app.group.service.AddressBookService;
import com.yedam.app.group.service.AddressBookVO;
import com.yedam.app.group.service.PageListVO;

@Service
public class AddressBookServiceImpl implements AddressBookService {

	private JavaMailSender javaMailSender;
    private AddressBookMapper addressBookMapper;

    
    public AddressBookServiceImpl(AddressBookMapper addressBookMapper, JavaMailSender javaMailSender) {
        this.addressBookMapper = addressBookMapper;
		this.javaMailSender = javaMailSender;
    }
	
	// 주소록검색기능
	@Override
	public PageListVO getPageList(PageListVO vo) {
		PageListVO result = addressBookMapper.getPageList(vo);
		return result;
	}

	//부서주소록
	@Override
	public List<AddressBookVO> AddressBookSelectAll(PageListVO vo) {
		return addressBookMapper.AddressBookFindAll(vo);
	}
	
	//개인주소록
	@Override
	public List<AddressBookVO> MyAddressBookSelectAll(PageListVO vo) {
		return addressBookMapper.MyAddressBookFindAll(vo);
	}

	//부서주소록 상세조회
	@Override
	public AddressBookVO AddressBookSelectInfo(AddressBookVO addressBookVO) {
		return addressBookMapper.AddressBookFindInfo(addressBookVO.getAddressBookId());
	}

	//개인주소록 상세조회
	@Override
	public AddressBookVO MyAddressBookSelectInfo(AddressBookVO addressBookVO) {
		return addressBookMapper.MyAddressBookFindInfo(addressBookVO.getAddressBookId());
	}

	//개인주소록 등록
	@Override
	public int AddressBookInsert(AddressBookVO addressBookVO) {
		int result = addressBookMapper.AddressBookCreate(addressBookVO);

		return result == 1 ? addressBookVO.getAddressBookId() : -1;
	}

	//개인주소록 수정
	@Override
	public Map<String, Object> AddressBookUpdate(AddressBookVO addressBookVO) {
	    Map<String, Object> map = new HashMap<>();
	    boolean isSuccessed = false;

	    // 먼저 기존 주소록 정보를 가져옵니다.
	    AddressBookVO existingBook = addressBookMapper.AddressBookFindInfo(addressBookVO.getAddressBookId());

	    // 기존 정보와 입력 받은 정보가 동일한지 확인
	    if (existingBook != null && !existingBook.equals(addressBookVO)) {
	        // 기존 정보와 다르면 수정 진행
	        int result = addressBookMapper.AddressBookModify(addressBookVO);

	        if (result == 1) {
	            isSuccessed = true;
	        }
	    } else {
	        // 값이 변경되지 않거나, 이미 최신 정보라면 수정하지 않음
	        isSuccessed = true;
	    }

	    map.put("result", isSuccessed);
	    map.put("target", addressBookVO);

	    return map;
	}

	//개인주소록 삭제
	@Override
	public Map<String, Object> AddressBookDelete(int AddressBookId) {
		Map<String, Object> map = new HashMap<>();

		int result = addressBookMapper.AddressBookRemove(AddressBookId);

		if (result == 1) {
			map.put("addressBookId", AddressBookId);
		}

		return map;
	}

	//주소록 페이지네이션
	@Override
	public int pageGetCount(PageListVO pagelistVO) {
		return addressBookMapper.getCount(pagelistVO);
	}
	
	//주소록 페이지네이션
	@Override
	public int pageGetCounts(PageListVO pagelistVO) {
		return addressBookMapper.getCounts(pagelistVO);
	}


}
