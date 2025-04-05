package com.yedam.app.group.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.yedam.app.group.mapper.MailMapper;
import com.yedam.app.group.service.MailService;
import com.yedam.app.group.service.MailVO;
import com.yedam.app.group.service.PageListVO;

@Service
public class MailServiceImpl implements MailService {

	private JavaMailSender javaMailSender;
	private MailMapper mailMapper;

	public MailServiceImpl(MailMapper mailMapper, JavaMailSender javaMailSender) {
		this.mailMapper = mailMapper;
		this.javaMailSender = javaMailSender;
	}

	// 메일검색기능
	@Override
	public PageListVO getPageList(PageListVO vo) {
		PageListVO result = mailMapper.getPageList(vo);
		return result;
	}

	// 메일전체조회
	@Override
	public List<MailVO> MailSelectAll(PageListVO vo) {
		return mailMapper.MailFindAll(vo);
	}

	// 메일상세조회
	@Override
	public MailVO MailSelectInfo(MailVO mailVO) {
		return mailMapper.MailFindInfo(mailVO.getMailId());
	}
	
	//등록된 파일 갯수
	@Override
	public MailVO FileCount(Integer mailId) {
		return mailMapper.MailFindInfo(mailId);
	}

	// 메일등록
	@Override
	public int InsertMail(MailVO mailVO) {
		int result = mailMapper.mailCreate(mailVO);
		return result == 1 ? mailVO.getMailId() : -1;
	}

	// 메일검색기록
	@Override
	public int MailRecodeInfo(MailVO mailVO) {
		return mailMapper.MailRecode(mailVO);
	}

	// 메일답장
	@Override
	public int MailPutInfo(MailVO mailVO) {
		int result = mailMapper.MailPutDate(mailVO);
		return result == 1 ? mailVO.getMailId() : -1;
	}

	// 메일전달
	@Override
	public int MailVeryInfo(MailVO mailVO) {
		int result = mailMapper.MailVeryDate(mailVO);
		return result == 1 ? mailVO.getMailId() : -1;
	}

	// 메일단건삭제
	@Override
	public Map<String, Object> MailDel(int mailId) {
		Map<String, Object> map = new HashMap<>();

		int result = mailMapper.MailDel(mailId);

		if (result == 1) {
			map.put("mailId", mailId);
		}
		return map;
	}

	// 여러개의 메일삭제
	@Override
	public void MailDels(List<Integer> mailIds) {

		if (mailIds != null && !mailIds.isEmpty()) {
            mailMapper.MailDels(mailIds); // Mapper를 통해 데이터베이스에서 삭제
        }
	}

	// 메일페이지네이션
	@Override
	public int pageGetCount(PageListVO pagelistVO) {
		return mailMapper.getCount(pagelistVO);
	}

//메일상세함

	// 받은메일함
	@Override
	public List<MailVO> selectGetList(PageListVO vo) {
		return mailMapper.selectGetList(vo);
	}

	// 보낸메일함
	@Override
	public List<MailVO> selectPutList(PageListVO vo) {
		return mailMapper.selectPutList(vo);
	}

	// 임시메일함
	@Override
	public List<MailVO> selectTemList(PageListVO vo) {
		return mailMapper.selectTemList(vo);
	}
	//임시저장
	@Override
	public Map<String, Object> MailPro(int mailId) {
		Map<String, Object> map = new HashMap<>();

		int result = mailMapper.MailPro(mailId);

		if (result == 1) {
			map.put("mailId", mailId);
		}
		return map;
	}

	// 휴지통
	@Override
	public List<MailVO> selectDelList(PageListVO vo) {
		return mailMapper.selectDelList(vo);
	}

	// 메일전송
	@Value("${spring.mail.username}")
	private String ADMIN_SEND_EMAIL;

	public String sendMailToUser(MailVO vo) {
		if("임시".equals(vo.getMailType())) {
			
			mailMapper.mailCreate(vo);
			return "성공";
		}
		
	    if (vo.getGetUser() == null || vo.getGetUser().isEmpty()) {
	        return "수신자의 이메일 주소가 유효하지 않습니다.";
	    }
	    
		//if (vo.getMailType().equals("보낸")) {
		
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(vo.getEmployeeId()); // 이메일을 보낼 송신자
			message.setTo(vo.getGetUser()); // 이메일을 받을 수신자
			message.setSubject(vo.getTitle()); // 이메일 제목
			message.setText(vo.getContent()); // 이메일 본문
			try {
				javaMailSender.send(message);

			} catch (MailException e) {
				e.printStackTrace();
				return "전송 실패";
			}
		//}
		
		if(!"전달".equals(vo.getMailType())) {
			//보낸
			vo.setMailType("보낸");  //   id -> getUser
			mailMapper.mailCreate(vo);
		}
		
		//받은
		vo.setMailType("받은");  //
		mailMapper.mailCreate(vo);

		return "전송성공";
	}

//기타

	// 검색기능
	@Override
	public List<MailVO> searchMails(MailVO mailVO) {
		return mailMapper.searchMails(mailVO);
	}

	// 임시메일 자동삭제 기능
	public void deleteExpiredMails() {
		mailMapper.deleteExpiredMails(); // 만료된 메일 삭제
	}

	// 휴지통 자동삭제 기능
	public void deleteCurrentMails() {
		mailMapper.deleteCurrentMails(); // 만료된 메일 삭제
	}
	
	// 휴지통상세조회
		@Override
		public MailVO deleteFindInfo(MailVO mailVO) {
			return mailMapper.deleteFindInfo(mailVO.getMailId());
		}

	@Scheduled(cron = "0 07 18 * * ?")
	public void scheduledDeleteExpiredMails() {
		deleteExpiredMails();
		deleteCurrentMails();
	}
	
	// 메일삭제
	@Override
	public Map<String, Object> MailRemove(int mailId) {
		Map<String, Object> map = new HashMap<>();

		int result = mailMapper.MailDel(mailId);

		if (result == 1) {
			map.put("mailId", mailId);
		}
		return map;
	}



}