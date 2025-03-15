package com.yedam.app.group.mapper;

import java.util.List;

import com.yedam.app.group.service.MailVO;

public interface MailMapper {

	//메일전체조회
	public List<MailVO> selectAllList();
	
	//메일상세조회
	public MailVO selectMailId();
	
	//나의 메일상세조회
	public MailVO MySelectMailId();
	
	//메일등록
	public int insertMailInfo(MailVO mailVO);
	
	//메일수정
	public int UpdateMail(MailVO mailVO);
	
	//메일답장
	public int PutDateMail(MailVO mailVO);
	
	//메일삭제
	public int DeleteMail(MailVO mailVO);
}
