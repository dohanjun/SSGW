package com.yedam.app.group.service;

import java.util.List;

public interface MailService {
	
	//메일전체조회
	public List<MailVO> findAll();
	
	//메일상세조회
	public MailVO findMailId();
	
	//나의 메일상세조회
	public MailVO MyFindMailId();
	
	//메일등록
	public int addInfo(MailVO mailVO);
	
	//메일수정
	public int UpdInfo(MailVO mailVO);
	
	//메일답장
	public int PutInfo(MailVO mailVO);
	
	//메일삭제
	public int DelInfo(MailVO mailVO);
	
}
