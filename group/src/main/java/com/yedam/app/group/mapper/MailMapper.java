package com.yedam.app.group.mapper;

import java.time.LocalDateTime;
import java.util.List;

import com.yedam.app.group.service.MailVO;
import com.yedam.app.group.service.PageListVO;

public interface MailMapper {
  
	
	//메일검색기능
	public PageListVO getPageList(PageListVO vo);
	
	//메일전체조회
	public List<MailVO> MailFindAll(PageListVO vo);
	
	//메일단건조회
	public MailVO MailFindInfo(int mailId);

	
	//메일등록
	public int mailCreate(MailVO mailVO);	
	
	//메일받기
	public void receiveEmails(MailVO vo);
	
	//메일검색기록
	public int MailRecode(MailVO mailVO);
	
	//메일답장
	public int MailPutDate(MailVO mailVO);
	
	//메일전달
	public int MailVeryDate(MailVO mailVO);
	
	//단건메일삭제
	public int MailRemove(int mailId);
	
	//여러개의 메일삭제
	public int MailRemoves(List<Integer> mailIds);
	
	//메일페이지네이션
	public int getCount(PageListVO pagelistVO);
	
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

	//주소검색 기능
	public List<MailVO> searchMails(MailVO mailVO);
	
	//임시메일함 자동삭제 기능
	void deleteExpiredMails(LocalDateTime  currentDateTime);
	
	//휴지통 자동삭제 기능
	void deleteCurrentMails(LocalDateTime  currentDateTime);

}
