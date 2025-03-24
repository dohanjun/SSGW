package com.yedam.app.group.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
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

	// 주소록전체조회
	@Override
	public List<AddressBookVO> AddressBookSelectAll(PageListVO vo) {
		return addressBookMapper.AddressBookFindAll(vo);
	}

	// 주소록상세조회
	@Override
	public AddressBookVO AddressBookSelectInfo(AddressBookVO addressBookVO) {
		return addressBookMapper.AddressBookFindInfo(addressBookVO.getAddressBookId());
	}

	// 나의주소록상세조회
	@Override
	public AddressBookVO MyAddressBookSelectInfo(AddressBookVO addressBookVO) {
		return addressBookMapper.MyAddressBookFindInfo(addressBookVO.getAddressBookId());
	}

	// 주소록등록
	@Override
	public int AddressBookInsert(AddressBookVO addressBookVO) {
		int result = addressBookMapper.AddressBookCreate(addressBookVO);

		return result == 1 ? addressBookVO.getAddressBookId() : -1;
	}

	// 주소록검색기록
	@Override
	public int AddressBookRecodeInfo(AddressBookVO addressBookVO) {
		return addressBookMapper.AddressBookRecode(addressBookVO);
	}

	// 주소록수정
	@Override
	public Map<String, Object> AddressBookUpdate(AddressBookVO addressBookVO) {
		Map<String, Object> map = new HashMap<>();
		boolean isSuccessed = false;

		int result = addressBookMapper.AddressBookModify(addressBookVO);

		if (result == 1) {
			isSuccessed = true;
		}

		map.put("result", isSuccessed);
		map.put("target", addressBookVO);

		AddressBookVO findVO = (AddressBookVO) map.get("target");

		return map;
	}

	// 주소록삭제
	@Override
	public Map<String, Object> AddressBookDelete(int AddressBookId) {
		Map<String, Object> map = new HashMap<>();

		int result = addressBookMapper.AddressBookRemove(AddressBookId);

		if (result == 1) {
			map.put("addressBookId", AddressBookId);
		}

		return map;
	}

	// 주소록페이지네이션
	@Override
	public int pageGetCount(PageListVO pagelistVO) {
		return addressBookMapper.getCount(pagelistVO);
	}

}
