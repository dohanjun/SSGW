package com.yedam.app.group.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

import com.yedam.app.group.mapper.AddressBookMapper;
import com.yedam.app.group.service.AddressBookService;
import com.yedam.app.group.service.BookVO;
import com.yedam.app.group.service.PageListVO;

public class AddressBookServiceImpl implements AddressBookService {

	private JavaMailSender javaMailSender;
	private AddressBookMapper addressBookMapper;


	@Autowired
	public AddressBookServiceImpl(AddressBookMapper addressBookMapper, JavaMailSender javaMailSender) {
		this.addressBookMapper = addressBookMapper;
		this.javaMailSender = javaMailSender;
	}
	
	@Override
	public List<BookVO> BookselectAll(PageListVO vo) {
		return addressBookMapper.BookfindAll(vo);
	}

	@Override
	public int pageGetCount(PageListVO pagelistVO) {
		// TODO Auto-generated method stub
		return 0;
	}

}
