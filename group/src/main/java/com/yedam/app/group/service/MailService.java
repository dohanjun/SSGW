package com.yedam.app.group.service;

import java.util.List;
import java.util.Map;

public interface MailService {
	
	//메일검색기능
	public PageListVO getPageList(PageListVO vo);
	
	//메일전체조회
	public List<MailVO> MailSelectAll(PageListVO vo);
	
	//메일단건조회
	public MailVO MailSelectInfo(MailVO mailVO);
	
	//메일등록
	public int InsertMail(MailVO mailVO);
	
	//메일보내기
	public String sendMailToUser(MailVO vo);
	
	//메일검색기록
	public int MailRecodeInfo(MailVO mailVO);
	
	//메일답장
	public int MailPutInfo(MailVO mailVO);
	
	//메일전달
	public Map<String, Object> MailVeryInfo(MailVO mailVO);
	
	//메일삭제
	public Map<String, Object> MailDelete(int mailId);

	//메일 페이지네이션
	public int pageGetCount(PageListVO pagelistVO);
	
//메일상세함
	
	//받은메일함
	public List<MailVO> selectGetList(PageListVO vo);
	//보낸메일함
	public List<MailVO> selectPutList(PageListVO vo);
	//임시메일함
	public List<MailVO> selectTemList(PageListVO vo);
	//휴지통
	public List<MailVO> selectDelList(PageListVO vo);
	
//기타
	//주소검색기능
	public List<MailVO> searchMails(MailVO mailVO);
	
	//임시메일함
	void deleteExpiredMails();
	
	//휴지통 자동삭제 기능
	void deleteCurrentMails();

}

