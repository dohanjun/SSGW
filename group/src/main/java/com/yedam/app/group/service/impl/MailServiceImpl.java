package com.yedam.app.group.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yedam.app.group.mapper.MailMapper;
import com.yedam.app.group.service.MailService;
import com.yedam.app.group.service.MailVO;

@Service
public class MailServiceImpl implements MailService {

	private MailMapper mailMapper;
	
	@Autowired
	public MailServiceImpl(MailMapper mailMapper) {
		this.mailMapper = mailMapper;
	}
	
	//메일전체조회
	@Override
	public List<MailVO> findAll() {
		return mailMapper.selectAllList();
	}
	
	//메일상세조회
	@Override
	public MailVO findMailId() {
		return mailMapper.selectMailId();
	}
	
	//나의메일상세조회
	@Override
	public MailVO MyFindMailId() {
		return mailMapper.MySelectMailId();
	}

	//메일등록
	@Override
	public int addInfo(MailVO mailVO) {
		return mailMapper.insertMailInfo(mailVO);
	}

	//메일수정
	@Override
	public int UpdInfo(MailVO mailVO) {
		return mailMapper.UpdateMail(mailVO);
	}

	//메일답장
	@Override
	public int PutInfo(MailVO mailVO) {
		return mailMapper.PutDateMail(mailVO);
	}

	//메일삭제
	@Override
	public int DelInfo(MailVO mailVO) {
		return mailMapper.DeleteMail(mailVO);
	}

}
