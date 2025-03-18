package com.yedam.app.group.service;

import java.util.List;
import java.util.Map;

public interface MailService {
	
	//메일검색기능
	public MailVO mailCondition(MailVO mailVO);
	
	//메일전체조회
	public List<MailVO> findAll(PageListVO vo);
	
	//메일단건조회
	public MailVO findMailId(MailVO mailVO);
	
	//나의메일단건조회
	public MailVO MyFindMailId(MailVO mailVO);
	
	//메일등록
	public int addInfo(MailVO mailVO);
	
	//메일수정
	public Map<String, Object> modifyUpdInfo(MailVO mailVO);
	
	//메일검색기록
	public int RecodeInfo(MailVO mailVO);
	
	//메일답장
	public int PutInfo(MailVO mailVO);
	
	//메일전달
	public Map<String, Object> VeryInfo(MailVO mailVO);
	
	//메일삭제
	public Map<String, Object> removeDelInfo(int mailId);

	//메일 페이지네이션
	public int pageGetCount(PageListVO pagelistVO);
	
}
