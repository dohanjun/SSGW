package com.yedam.app.group.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yedam.app.group.mapper.MailMapper;
import com.yedam.app.group.service.MailService;
import com.yedam.app.group.service.MailVO;
import com.yedam.app.group.service.PageListVO;

@Service
public class MailServiceImpl implements MailService {

	private MailMapper mailMapper;
	
	@Autowired
	public MailServiceImpl(MailMapper mailMapper) {
		this.mailMapper = mailMapper;
	}
	
	//메일검색기능
	@Override
	public MailVO mailCondition(MailVO mailVO) {
		return mailMapper.mailCondition(mailVO);
	}
	
	//메일전체조회
	@Override
	public List<MailVO> findAll(PageListVO vo) {
		return mailMapper.selectAllList(vo);
	}
	
	//메일상세조회
	@Override
	public MailVO findMailId(MailVO mailVO) {
		return mailMapper.selectMailId(mailVO.getMailId());
	}
	
	//나의메일상세조회
	@Override
	public MailVO MyFindMailId(MailVO mailVO) {
		return mailMapper.MySelectMailId(mailVO.getMailId());
	}

	//메일등록
	@Override
	public int addInfo(MailVO mailVO) {
		int result = mailMapper.insertMailInfo(mailVO);
		
		return result == 1 ? mailVO.getMailId() : -1;
	}
	
	//메일검색기록
	@Override
	public int RecodeInfo(MailVO mailVO) {
		return mailMapper.RecodeMail(mailVO);
	}

	//메일수정
	@Override
	public Map<String, Object> modifyUpdInfo(MailVO mailVO) {
		Map<String, Object> map = new HashMap<>();
		boolean isSuccessed = false;
		
		int result = mailMapper.UpdateMail(mailVO);
		
		if(result == 1) {
			isSuccessed = true;
		}
		
		map.put("result", isSuccessed);
		map.put("target", mailVO);
		
		MailVO findVO = (MailVO) map.get("target");
		
		return map;
	}

	//메일답장
	@Override
	public int PutInfo(MailVO mailVO) {
		int result = mailMapper.PutDateMail(mailVO);
		
		return result == 1 ? mailVO.getMailId() : -1;
	}
	
	//메일전달
	
	@Override
	public Map<String, Object> VeryInfo(MailVO mailVO) {
		Map<String, Object> map = new HashMap<>();
		boolean isSuccessed = false;
		
		int result = mailMapper.VeryDateMail(mailVO);
		
		if(result == 1) {
			isSuccessed = true;
		}
		
		map.put("result", isSuccessed);
		map.put("target", mailVO);
		
		MailVO findVO = (MailVO) map.get("target");
		
		return map;
	}

	//메일삭제
	@Override
	public Map<String, Object> removeDelInfo(int mailId) {
		Map<String, Object> map = new HashMap<>();
		
		int result = mailMapper.DeleteMail(mailId);
		
		if(result == 1) {
			map.put("mailId", mailId);
		}
		
		return map;
	}

	//메일페이지네이션
	@Override
	public int pageGetCount(PageListVO pagelistVO) {
		return mailMapper.getCount(pagelistVO);
	}


}
