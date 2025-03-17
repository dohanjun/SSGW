package com.yedam.app.group.mapper;

import java.util.List;

import com.yedam.app.group.service.MailVO;

public interface MailMapper {

	//메일전체조회
	public List<MailVO> selectAllList();
	
	//메일단건조회
	public MailVO selectMailId(int mailId);
	
	//나의 메일단건조회
	public MailVO MySelectMailId(int mailId);
	
	//메일등록
	public int insertMailInfo(MailVO mailVO);
	
	//메일수정
	public int UpdateMail(MailVO mailVO);
	
	//메일검색기록
	public int RecodeMail(MailVO mailVO);
	
	//메일답장
	public int PutDateMail(MailVO mailVO);
	
	//메일전달
	public int VeryDateMail(MailVO mailVO);
	
	//메일삭제
	public int DeleteMail(int mailId);
}
