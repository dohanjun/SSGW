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
	public PageListVO getPageList(PageListVO vo) {
		PageListVO result = mailMapper.getPageList(vo);
		return result;
	}
	
	//메일전체조회
	@Override
	public List<MailVO> selectAll(PageListVO vo) {
		return mailMapper.findAll(vo);
	}
	
	//메일상세조회
	@Override
	public MailVO selectInfo(MailVO mailVO) {
		return mailMapper.findInfo(mailVO.getMailId());
	}
	
	//나의메일상세조회
	@Override
	public MailVO MySelectInfo(MailVO mailVO) {
		return mailMapper.MyfindInfo(mailVO.getMailId());
	}

	//메일등록
	@Override
	public int insert(MailVO mailVO) {
		int result = mailMapper.create(mailVO);
		
		return result == 1 ? mailVO.getMailId() : -1;
	}
	
	//메일검색기록
	@Override
	public int RecodeInfo(MailVO mailVO) {
		return mailMapper.RecodeMail(mailVO);
	}

	//메일수정
	@Override
	public Map<String, Object> update(MailVO mailVO) {
		Map<String, Object> map = new HashMap<>();
		boolean isSuccessed = false;
		
		int result = mailMapper.modify(mailVO);
		
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
	public Map<String, Object> delete(int mailId) {
		Map<String, Object> map = new HashMap<>();
		
		int result = mailMapper.remove(mailId);
		
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

//메일상세함
	
	//받은메일함
	@Override
	public List<MailVO> selectGetList(PageListVO vo) {
		return mailMapper.selectGetList(vo);
	}
	//보낸메일함
	@Override
	public List<MailVO> selectPutList(PageListVO vo) {
		return mailMapper.selectPutList(vo);
	}
	//임시메일함
	@Override
	public List<MailVO> selectTemList(PageListVO vo) {
		return mailMapper.selectTemList(vo);
	}
	//휴지통
	@Override
	public List<MailVO> selectDelList(PageListVO vo) {
		return mailMapper.selectDelList(vo);
	}



}
