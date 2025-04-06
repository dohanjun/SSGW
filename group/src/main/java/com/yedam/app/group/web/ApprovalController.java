package com.yedam.app.group.web;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yedam.app.group.service.AlarmService;
import com.yedam.app.group.service.AlarmVO;
import com.yedam.app.group.service.ApprovalFormVO;
import com.yedam.app.group.service.ApprovalService;
import com.yedam.app.group.service.ApprovalVO;
import com.yedam.app.group.service.AprvFileService;
import com.yedam.app.group.service.AprvFileVO;
import com.yedam.app.group.service.AprvRoutesVO;
import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.EmpVO;
import com.yedam.app.group.service.VacationRequestVO;
import com.yedam.app.group.service.VacationService;
import com.yedam.app.group.service.VacationVO;

import lombok.Data;



/** 전자결재 관리
 * @author 김상연
 * @since 2025-03-24
 * <pre>
 * <pre>
 * 수정일자     수정자    수정내용
 * ---------------------------
 * </pre>
 * </pre>
 */
@Controller
@Data
public class ApprovalController {

	private final ApprovalService approvalService;
	private final EmpService empService;
	private final AprvFileService aprvFileService;
	private final VacationService vacationService;
	private final AlarmService alarmService;
	
    @Value("${file.upload-dir}")  
    private String uploadDir;  // 현재 프로젝트 안에서 저장하는 폴더 설정
	
	/** aprv_routes의 사원번호사용(결재자)
	 * aprv_status로 문서함조회(대기, 진행, 완료, 반려)
	 * @param aprvVO
	 * @param model
	 * @return 문서조회페이지명
	 */
    @GetMapping("aprv/list")
    public String aprvList(@RequestParam(defaultValue = "1") int page,
                           ApprovalVO aprvVO, Model model) {

        if (aprvVO.getAprvStatus() == null || aprvVO.getAprvStatus().trim().isEmpty()) {
            aprvVO.setAprvStatus("대기");
        }

        EmpVO loggedInUser = empService.getLoggedInUserInfo();
        if (loggedInUser != null) {
            aprvVO.setEmployeeNo(loggedInUser.getEmployeeNo());
            aprvVO.setSuberNo(loggedInUser.getSuberNo());

            // 페이징 정보 설정
            aprvVO.setPage(page);
            aprvVO.setSize(10);
            aprvVO.setOffset((page - 1) * aprvVO.getSize());

            int totalCount = approvalService.countAprvListByStatus(aprvVO);
            int totalPages = (int) Math.ceil((double) totalCount / aprvVO.getSize());
            if (totalPages == 0) {
                totalPages = 1;
            }
            
            List<ApprovalVO> list = approvalService.findAprvListByStatus(aprvVO);

            // 참조 제외
            list = list.stream()
                       .filter(a -> !"참조".equals(a.getAprvRole()))
                       .collect(Collectors.toList());

            model.addAttribute("aprvs", list);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("aprvStatus", aprvVO.getAprvStatus());
            model.addAttribute("searchTitle", aprvVO.getTitle());
            model.addAttribute("searchEmployeeName", aprvVO.getEmployeeName());
            model.addAttribute("searchDraftDate", aprvVO.getDraftDate());

            ApprovalVO stamp = approvalService.getActiveStamp(aprvVO);
            model.addAttribute("stampImgPath", (stamp != null) ? stamp.getStampImgPath() : "");
        }

        if ("참조".equals(aprvVO.getAprvRole())) {
            return "group/approval/approval_reference";
        }

        // 상태별 페이지 반환
        if ("완료".equals(aprvVO.getAprvStatus())) {
            return "group/approval/approval_complete";
        } else if ("진행".equals(aprvVO.getAprvStatus())) {
            return "group/approval/approval_progress";
        } else if ("반려".equals(aprvVO.getAprvStatus())) {
            return "group/approval/approval_return";
        } else {
            return "group/approval/pending_document";
        }
    }
	
    // 회사 전용 양식 목록 페이지
    @GetMapping("aprv/forms")
    public String showFormList(Model model) {
        // 로그인한 사용자 정보에서 회사번호(suberNo) 가져오기
        EmpVO loggedInUser = empService.getLoggedInUserInfo();
        int suberNo = loggedInUser.getSuberNo();  // 회사 번호

        // ApprovalFormVO 객체에 회사번호를 설정
        ApprovalFormVO approvalFormVO = new ApprovalFormVO();
        approvalFormVO.setSuberNo(suberNo);

        // 해당 회사번호에 맞는 양식 목록 조회
        List<ApprovalFormVO> formList = approvalService.findAllAprvForms(approvalFormVO);
        // 모델에 양식 목록 추가
        model.addAttribute("formList", formList);

        // 양식 목록을 보여주는 HTML로 이동
        return "group/approval/approval_forms"; // Thymeleaf 템플릿 파일 이름
    }
    
    // 회사양식 상세 페이지
    @GetMapping("aprv/detail/{formId}")
    public String showFormDetail(@PathVariable("formId") int formId, Model model) {
        ApprovalFormVO form = approvalService.getAprvFormById(formId);
        model.addAttribute("form", form);
        return "group/approval/approval_form_detail";
    }

    // 회사양식 수정 
    @PostMapping("aprv/update")
    public String updateForm(@ModelAttribute ApprovalFormVO formVO) {
    	if ("Y".equals(formVO.getActive())) {
            formVO.setActive("1");
        } else {
            formVO.setActive("0");
        }
    	
        approvalService.updateAprvForm(formVO);
        return "redirect:/aprv/forms";
    }

    // 회사양식 삭제
    @GetMapping("aprv/delete/{formId}")
    public String deleteForm(@PathVariable("formId") int formId) {
        approvalService.deleteAprvForm(formId);
        return "redirect:/aprv/forms";
    }
    
    
    
    
    
	/**
	 * aprv_routes테이블에서 aprv_role이 '참조'인 사원만 볼 수 있는 페이지
	 * @param aprvVO
	 * @param model
	 * @return 참조열람함페이지
	 */
	@GetMapping("aprv/reference")
	public String aprvReferenceList(@RequestParam(defaultValue = "1") int page, ApprovalVO aprvVO, Model model) {
	    // 로그인한 사용자 정보 가져오기
	    EmpVO loggedInUser = empService.getLoggedInUserInfo();
	    if (loggedInUser != null) {
	        aprvVO.setEmployeeNo(loggedInUser.getEmployeeNo());
	        aprvVO.setSuberNo(loggedInUser.getSuberNo());
	        aprvVO.setAprvRole("참조");  // 참조자 역할만
	        
	        aprvVO.setPage(page);
	        aprvVO.setSize(10);
	        aprvVO.setOffset((page - 1) * aprvVO.getSize());

	        int totalCount = approvalService.countReferenceList(aprvVO);
	        int totalPages = (int) Math.ceil((double) totalCount / aprvVO.getSize());
	        if (totalPages == 0) totalPages = 1;
	        
	        List<ApprovalVO> list = approvalService.findAprvListByRole(aprvVO);
	        model.addAttribute("aprvs", list);
	        
	        model.addAttribute("currentPage", page);
	        model.addAttribute("totalPages", totalPages);
	        
	        model.addAttribute("searchTitle", aprvVO.getTitle());
	        model.addAttribute("searchEmployeeName", aprvVO.getEmployeeName());
	        model.addAttribute("searchDraftDate", aprvVO.getDraftDate());
	        
	        // 활성화된 도장 정보 가져오기
	        ApprovalVO stamp = approvalService.getActiveStamp(aprvVO);
	        model.addAttribute("stampImgPath", (stamp != null) ? stamp.getStampImgPath() : "");
	    }
	    
	    return "group/approval/approval_reference";  // 참조열람함 페이지
	}


	
	/**
	 * 사원번호는 aprv_documents의 emplyoee_no(기안자)
	 * @param aprvVO
	 * @param model
	 * @return 결재요청함, 임시저장함
	 */
	// 결재요청함, 임시저장함
	@GetMapping("aprv/request")
	public String aprvRequestList(@RequestParam(defaultValue = "1") int page, ApprovalVO aprvVO, Model model) {
	    // 로그인한 사용자 정보 가져오기
	    EmpVO loggedInUser = empService.getLoggedInUserInfo();
	    
	    if (loggedInUser == null) {
	    	return "redirect:/";  // 로그인하지 않은 경우 홈 페이지로 리디렉션
	    }
	    	
        aprvVO.setEmployeeNo(loggedInUser.getEmployeeNo());  // 로그인한 사용자 정보 설정
        aprvVO.setSuberNo(loggedInUser.getSuberNo());        // 로그인한 사용자 회사번호
        
        // 페이징 세팅
        aprvVO.setPage(page);
        aprvVO.setSize(10);  // 한 페이지당 10개
        aprvVO.setOffset((page - 1) * aprvVO.getSize());
        
        int totalCount = approvalService.countAllList(aprvVO);
        int totalPages = (int) Math.ceil((double) totalCount / aprvVO.getSize());
        
        List<ApprovalVO> list = approvalService.findAllList(aprvVO);

        //  공통 model 추가
        model.addAttribute("aprvs", list);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("aprvStatus", aprvVO.getAprvStatus());
        model.addAttribute("searchTitle", aprvVO.getTitle());
        model.addAttribute("searchAprvStatus", aprvVO.getAprvStatus());
        model.addAttribute("searchDraftDate", aprvVO.getDraftDate());

        //  뷰 분기
        return "임시".equals(aprvVO.getAprvStatus())
                ? "group/approval/approval_save"
                : "group/approval/approval_request";
	}


	/**
	 * 기존에 저장된 기본양식, 회사전용 양식을 불러와서 기안문 작성
	 * @param aprvVO
	 * @param aprvformVO
	 * @param model
	 * @return 기안문작성페이지
	 */
	// 기안문 작성 페이지
	@GetMapping("aprv/write")
	public String aprvWrite(ApprovalVO aprvVO, ApprovalFormVO aprvformVO, Model model) {
	    // 임시저장된 문서가 있을 경우 해당 문서의 정보 가져오기
	    if (aprvVO.getDraftNo() != null) {
	        // 임시저장된 문서의 정보 가져오기
	        ApprovalVO savedDocument = approvalService.findAprvInfo(aprvVO);
	        
	        if (savedDocument != null) {
	            model.addAttribute("aprvVO", savedDocument); // 문서 정보를 모델에 추가
	        }
	    }
	    return "group/approval/approval_writing"; // 기안문 작성 페이지로 이동
	}
	
	/**
	 * 기안문 작성페이지에서 등록 버튼 클릭시 aprv_status가 '대기'로 db에 저장, 임시저장시 aprv_status가 '임시'로 저장
	 * @param approvalVO
	 * @param routeVO
	 * @param status
	 * @return 기안문 상신 기능
	 */
	// 기안문 상신
	@PostMapping(value = "/aprv/writing", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String submitApproval(@ModelAttribute ApprovalVO approvalVO,
	                             @RequestParam(value = "files", required = false) MultipartFile[] files) {
	    EmpVO loginUser = empService.getLoggedInUserInfo();
	    approvalVO.setEmployeeNo(loginUser.getEmployeeNo());
	    approvalVO.setSuberNo(loginUser.getSuberNo());

	    // 문서 상태 설정
	    if ("임시".equals(approvalVO.getAprvStatus())) {
	        approvalVO.setAprvStatus("임시");
	    } else {
	        approvalVO.setAprvStatus("대기");
	    }

	    approvalService.createAprvDocu(approvalVO);
	    
	    int draftNo = approvalVO.getDraftNo();
	    
	 //  휴가원 양식일 경우 vacation_request 테이블 insert
	    if ("휴가원".equals(approvalVO.getFormType())) {
	        VacationRequestVO vacationVO = new VacationRequestVO();
	        vacationVO.setDraftNo(draftNo);
	        vacationVO.setEmployeeNo(loginUser.getEmployeeNo());
	        vacationVO.setVacationTypeId(approvalVO.getVacationTypeId());
	        // 날짜 변환
	        try {
	            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	            vacationVO.setStartVacationDate(sdf.parse(approvalVO.getStartDate()));
	            vacationVO.setEndVacationDate(sdf.parse(approvalVO.getEndDate()));
	            vacationVO.setUsedVacation(Integer.parseInt(approvalVO.getUsedVacation()));
	            approvalService.createVacation(vacationVO);
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }
	    }
	    

	    // 첨부파일 저장 추가
	    if (files != null && files.length > 0) {
	        aprvFileService.insertFiles(draftNo, files);
	    }

	    // 결재자 저장
	    List<Integer> approvers = approvalVO.getApprovers();
	    if (!"임시".equals(approvalVO.getAprvStatus()) && approvers != null && !approvers.isEmpty()) {
	    	for (int i = 0; i < approvers.size(); i++) {
	    		AprvRoutesVO r = new AprvRoutesVO();
	    		r.setDraftNo(draftNo);
	    		r.setAprvOrder(String.valueOf(i + 1));
	    		r.setAprvRole("결재");
	    		r.setAprvStatus("대기");
	    		r.setEmployeeNo(approvers.get(i));
	    		approvalService.createAprvRout(r);
	    		
	    		// 알림 추가 (결재자에게)
	    		AlarmVO alarm = new AlarmVO();
	    		alarm.setAlarmMessage("결재 요청이 있습니다!");
	    		alarm.setAlarmType("결재");
	    		alarm.setRead("N");
	    		alarm.setEmployeeNo(approvers.get(i)); // 결재자로 지정된 사원번호
	    		alarm.setAlarmIcon("fa-file-signature"); // 아이콘 클래스 (원하는 대로)

	    		alarmService.insertAlarm(alarm);
	    	}	    	
	    }

	    // 참조자 저장
	    List<Integer> reference = approvalVO.getReferences();
	    if (reference != null && !reference.isEmpty()) {
	        for (Integer empNo : reference) {
	            AprvRoutesVO ref = new AprvRoutesVO();
	            ref.setDraftNo(draftNo);
	            ref.setAprvOrder("0");
	            ref.setAprvRole("참조");
	            ref.setAprvStatus("대기");
	            ref.setEmployeeNo(empNo);
	            approvalService.createAprvRout(ref);
	        }
	    }

	    if ("임시".equals(approvalVO.getAprvStatus())) {
	    	String encodedStatus = URLEncoder.encode("임시", StandardCharsets.UTF_8);
	        return "redirect:/aprv/request?aprvStatus=" + encodedStatus;
	    } else {
	        return "redirect:/aprv/list";
	    }
	}
	
	// 임시저장된 문서 삭제
	@PostMapping("/aprv/removeTemporaryData")
    @ResponseBody
    public Map<String, Object> removeTemporaryData(@RequestParam Integer draftNo) {
        Map<String, Object> result = new HashMap<>();

        try {
            approvalService.removeTemporaryData(draftNo);
            result.put("success", true);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }

        return result;
    }
	
	
	/**
	 * 
	 * 양식선택 모달창에서 선택한 양식의 content를 작성페이지 content 영역에 출력
	 * @param id
	 * @param type
	 * @param aprvVO
	 * @param aprvFormVO
	 * @return 양식목록불러오는기능
	 */
	@GetMapping("/aprvWriting/content/{id}")
	public ResponseEntity<Map<String, Object>> getFormContentById(
	        @PathVariable("id") int id,
	        @RequestParam("type") String type,
	        ApprovalVO aprvVO,
	        ApprovalFormVO aprvFormVO) {

	    Map<String, Object> response = new HashMap<>();

	    if ("basic".equalsIgnoreCase(type)) {
	        aprvVO.setBasicsFormId(id);
	        ApprovalVO basicForm = approvalService.findBasicsForm(aprvVO);

	        if (basicForm != null) {
	            response.put("content", basicForm.getContent());
	            response.put("formType", basicForm.getFormType());
	        } else {
	            response.put("error", "해당 기본 양식을 찾을 수 없습니다.");
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	        }

	    } else if ("company".equalsIgnoreCase(type)) {
	        aprvFormVO.setFormId(id);
	        EmpVO loginUser = empService.getLoggedInUserInfo();
	        aprvFormVO.setSuberNo(loginUser.getSuberNo()); // 보안상 회사번호 설정
	        ApprovalFormVO companyForm = approvalService.findAprvForm(aprvFormVO);

	        if (companyForm != null) {
	            response.put("content", companyForm.getContent());
	            response.put("formType", companyForm.getFormType());
	        } else {
	            response.put("error", "해당 회사 전용 양식을 찾을 수 없습니다.");
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	        }

	    } else {
	        response.put("error", "type 파라미터는 'basic' 또는 'company' 여야 합니다.");
	        return ResponseEntity.badRequest().body(response);
	    }

	    return ResponseEntity.ok(response);
	}
	
	/**
	 * 기안문 작성페이지 양식선택 버튼 클릭시 모달창에 양식 목록을 출력
	 * @param aprvVO
	 * @param aprvformVO
	 * @return 양식목록 모달창에 출력
	 */
	@GetMapping("/aprvWriting/content")
	public ResponseEntity<Map<String, Object>> getFormContent(ApprovalVO aprvVO, ApprovalFormVO aprvformVO) {
	    Map<String, Object> response = new HashMap<>();

	    // 기본 양식 가져오기
	    List<ApprovalVO> basicList = approvalService.findAllBasicsForm(aprvVO);
	    response.put("basicsForms", basicList); // 기본 양식 리스트 반환

	    // 로그인한 사용자 정보 가져오기
	    EmpVO loggedInUser = empService.getLoggedInUserInfo();
	    aprvformVO.setSuberNo(loggedInUser.getSuberNo());

	    // 회사 전용 양식 가져오기
	    List<ApprovalFormVO> subList = approvalService.findAllAprvForm(aprvformVO);
	    response.put("aprvForms", subList); // 회사 전용 양식 리스트 반환

	    return ResponseEntity.ok(response); // JSON 형식으로 반환
	}


	/**
	 * 도장등록기능 수행 파일은 D:/uploads/ 에 저장
	 * @param file
	 * @return 도장등록모달창 - 도장등록기능
	 */
	@PostMapping("aprv/upload")
	public ResponseEntity<Map<String, Object>> uploadStamp(@RequestParam("file") MultipartFile file) {
		Map<String, Object> response = new HashMap<>();

		if (file.isEmpty()) {
			response.put("success", false);
			response.put("message", "파일이 없습니다.");
			return ResponseEntity.badRequest().body(response);
		}

		try {

			// 파일 저장 경로 설정  
			String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
			Path filePath = Paths.get(uploadDir + "/" + fileName);

			// 파일 저장
			Files.createDirectories(filePath.getParent());
			Files.write(filePath, file.getBytes());

			// DB에 저장할 경로 설정
			String fileDbPath = "/uploads/" + fileName;

			// ApprovalVO 객체 생성 후 데이터 저장
			ApprovalVO aprvVO = new ApprovalVO();
			aprvVO.setStampImgPath(fileDbPath);

			// DB에 저장
			int result = approvalService.createStamp(aprvVO);

			if (result > 0) {
				response.put("success", true);
				response.put("imageUrl", fileDbPath);
			} else {
				response.put("success", false);
				response.put("message", "DB 저장 실패");
			}
			return ResponseEntity.ok(response);

		} catch (IOException e) {
			response.put("success", false);
			response.put("message", "파일 저장 중 오류 발생");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	/**
	 * 업데이트 시 기존에 등록된 도장은 active = '0' 비활성화, 새롭게 수정된 도장은 active = '1' 활성화 
	 * @param file
	 * @return 도장등록모달창 - 도장수정기능
	 */
	@PostMapping("aprv/modify")
	public ResponseEntity<Map<String, Object>> modifyStamp(@RequestParam("file") MultipartFile file) {
		Map<String, Object> response = new HashMap<>();

		if (file.isEmpty()) {
			response.put("success", false);
			response.put("message", "파일이 없습니다.");
			return ResponseEntity.badRequest().body(response);
		}

		try {

			// 파일 저장 경로 설정
			String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
			Path filePath = Paths.get(uploadDir , fileName);

			// 파일 저장
			Files.createDirectories(filePath.getParent());
			Files.write(filePath, file.getBytes());

			// DB에 저장할 경로 설정
			String fileDbPath = "/uploads/" + fileName;

			// ApprovalVO 객체 생성 후 데이터 설정
			ApprovalVO aprvVO = new ApprovalVO();
			aprvVO.setStampImgPath(fileDbPath);

			// 도장 수정 서비스 호출
			Map<String, Object> result = approvalService.modifyStamp(aprvVO);

			if ((boolean) result.get("success")) {
				response.put("success", true);
				response.put("imageUrl", fileDbPath);
			} else {
				response.put("success", false);
				response.put("message", "도장 수정 실패");
			}

			return ResponseEntity.ok(response);

		} catch (IOException e) {
			response.put("success", false);
			response.put("message", "파일 저장 중 오류 발생");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	/**
	 * 도장등록 모달창 삭제 버튼 클릭시 기존 도장 active = '0'
	 * @return 도장등록모달창 - 도장삭제(도장비활성화)
	 */
	@PostMapping("aprv/delete")
	public ResponseEntity<Map<String, Object>> deleteStamp() {
	    Map<String, Object> response = approvalService.removeStamp(new ApprovalVO());
	    return ResponseEntity.ok(response);
	}
	
	/**
	 * 기존에 활성화 된 도장을 확인해서 활성화 된 도장이 있다면 등록기능 사용 불가, 수정,삭제만 가능
	 * @return 도장등록모달창
	 */
	// 활성화된 도장 조회
	@GetMapping("aprv/stamp")
	public ResponseEntity<Map<String, Object>> getActiveStamp() {
	    Map<String, Object> response = new HashMap<>();
	    EmpVO loggedInUser = empService.getLoggedInUserInfo();

	    if (loggedInUser == null) {
	        response.put("success", false);
	        response.put("message", "로그인한 사용자 정보를 찾을 수 없습니다.");
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	    }

	    // ApprovalVO 객체 생성 후 employeeNo 설정
	    ApprovalVO aprvVO = new ApprovalVO();
	    aprvVO.setEmployeeNo(loggedInUser.getEmployeeNo());

	    ApprovalVO stamp = approvalService.getActiveStamp(aprvVO);

	    if (stamp != null && stamp.getStampImgPath() != null) {
	        response.put("success", true);
	        response.put("stampImgPath", stamp.getStampImgPath());
	    } else {
	        response.put("success", false);
	        response.put("message", "활성화된 도장이 없습니다.");
	    }

	    return ResponseEntity.ok(response);
	}
	
	// 도장확인
	@GetMapping("/aprv/checkStamp/{employeeNo}")
	@ResponseBody
	public Map<String, Object> checkActiveStamp(@PathVariable Integer employeeNo) {
	    Map<String, Object> result = new HashMap<>();

	    try {
	        // 활성화된 도장이 있는지 여부 조회
	        ApprovalVO aprvVO = new ApprovalVO();
	        aprvVO.setEmployeeNo(employeeNo);

	        ApprovalVO activeStamp = approvalService.getActiveStamp(aprvVO); // 이미 만든 메서드 활용

	        result.put("activeStamp", activeStamp != null);  // 도장이 있으면 true, 없으면 false
	        result.put("success", true);
	    } catch (Exception e) {
	        result.put("success", false);
	        result.put("message", e.getMessage());
	    }

	    return result;
	}

	
	/**
	 * 대기함, 진행함에서 문서 li태그를 클릭시 이동하는 페이지
	 * @param draftNo
	 * @param model
	 * @return 결재페이지
	 */
	@GetMapping("aprv/info")
	public String aprvInfo(AprvRoutesVO routVO, Model model) {
	    EmpVO loggedInUser = empService.getLoggedInUserInfo();
	    int loggedInEmpNo = loggedInUser.getEmployeeNo();
	    model.addAttribute("loggedInEmpNo", loggedInEmpNo);

	    ApprovalVO infoVO = new ApprovalVO();
	    infoVO.setDraftNo(routVO.getDraftNo());
	    ApprovalVO aprvVO = approvalService.findAprvInfo(infoVO);

	    routVO.setSuberNo(loggedInUser.getSuberNo());
	    int draftNo = routVO.getDraftNo();

	    List<AprvRoutesVO> routList = approvalService.findRoutes(routVO);
	    List<AprvFileVO> fileList = aprvFileService.findFilesByDraftNo(draftNo);
	    
	    
	    // aprvOrder, aprvRole 설정
	    String aprvOrder = routList.stream()
	            .filter(r -> r.getEmployeeNo() == loggedInEmpNo)
	            .map(AprvRoutesVO::getAprvOrder)
	            .findFirst()
	            .orElse("0");

	    String aprvRole = routList.stream()
	            .filter(r -> r.getEmployeeNo() == loggedInEmpNo)
	            .map(AprvRoutesVO::getAprvRole)
	            .findFirst()
	            .orElse("결재");

	    // 현재 결재 순서인지 판단
	    boolean isMyTurn = routList.stream()
	    	    .anyMatch(r -> r.getEmployeeNo() == loggedInEmpNo);

	    // 모델에 담기
	    model.addAttribute("aprv", aprvVO);
	    model.addAttribute("aprvroutes", routList);
	    model.addAttribute("files", fileList);
	    model.addAttribute("aprvOrder", aprvOrder);
	    model.addAttribute("aprvRole", aprvRole);
	    model.addAttribute("isMyTurn", isMyTurn);
	    model.addAttribute("aprv1", routList.stream().filter(r -> "1".equals(r.getAprvOrder())).findFirst().orElse(null));
	    model.addAttribute("aprv2", routList.stream().filter(r -> "2".equals(r.getAprvOrder())).findFirst().orElse(null));
	    model.addAttribute("aprv3", routList.stream().filter(r -> "3".equals(r.getAprvOrder())).findFirst().orElse(null));


	    
	    return "group/approval/approval";
	}

	
	// 완료함 상세 페이지
    @GetMapping("aprv/done")
    public String aprvDoneInfo(AprvRoutesVO routVO, Model model) {
        EmpVO loggedInUser = empService.getLoggedInUserInfo();
        model.addAttribute("loggedInEmpNo", loggedInUser.getEmployeeNo());

        ApprovalVO infoVO = new ApprovalVO();
        infoVO.setDraftNo(routVO.getDraftNo());

        ApprovalVO aprvVO = approvalService.findAprvInfo(infoVO);

        routVO.setSuberNo(loggedInUser.getSuberNo());
        List<AprvRoutesVO> routList = approvalService.findAprvRoutesForDone(routVO);
        List<AprvFileVO> fileList = aprvFileService.findFilesByDraftNo(routVO.getDraftNo());

        model.addAttribute("aprv", aprvVO);
        model.addAttribute("aprvroutes", routList);
        model.addAttribute("files", fileList);

        return "group/approval/approval_done"; // 완료함용 HTML
    }
	
	
	@PostMapping("/aprv/stampsave")
	@ResponseBody
	public Map<String, Object> registerStamp(@RequestBody AprvRoutesVO routVO) {
	    Map<String, Object> result = new HashMap<>();

	    try {
	    	// 1. 도장 정보 등록
	        approvalService.modifyStampForRoute(routVO);

	        // 2. 등록된 도장의 이미지 경로 다시 조회
	        ApprovalVO stampVO = new ApprovalVO();
	        stampVO.setEmployeeNo(routVO.getEmployeeNo());
	        ApprovalVO activeStamp = approvalService.getActiveStamp(stampVO);

	        result.put("stampImgPath", (activeStamp != null) ? activeStamp.getStampImgPath() : null);
	        result.put("success", true);
	    } catch (Exception e) {
	        result.put("success", false);
	        result.put("message", e.getMessage());
	    }

	    return result;
	}

	
	/**
	 * CKeditor로 양식을 추가하는 페이지
	 * @return 회사전용양식추가페이지
	 */
	@GetMapping("write")
	public String write() {
		return "group/approval/approval_form_create";
	}
	
	/**
	 * 
	 * @param aprvformVO
	 * @return 에디터에서 작성한 내용을 DB에 저장하는 기능
	 */
	@PostMapping("/saveForm")
	public String saveForm(ApprovalFormVO aprvformVO) {
		
		EmpVO loggedInUser = empService.getLoggedInUserInfo();
		
		aprvformVO.setSuberNo(loggedInUser.getSuberNo());
		
		int formId = approvalService.createForm(aprvformVO);
		
		String url = null;

		if (formId > 0) {
			url = "redirect:/aprv/list"; // 저장 후 결재 대기함 페이지로 리디렉션
		} else {
			url = "redirect:/errorPage"; // 저장 실패 시 에러 페이지로 리디렉션
		}

		return url;
	}
	
	// 승인시 '진행', '완료'로 update
	@PostMapping("/aprv/approve")
	@ResponseBody
	public Map<String, Object> approve(@RequestBody AprvRoutesVO vo) {
	    Map<String, Object> result = new HashMap<>();
	    try {
	        // 최대 결재 순서를 가져옴 (숫자로 비교하기 위해 파싱)
	        String maxAprvOrderStr = approvalService.getMaxAprvOrder(vo.getDraftNo());
	        
	        // maxAprvOrder가 빈 문자열일 경우 0으로 처리
	        int maxAprvOrder = maxAprvOrderStr.isEmpty() ? 0 : Integer.parseInt(maxAprvOrderStr);

	        // 현재 결재자가 맡고 있는 순서를 정수로 변환
	        // vo.getAprvOrder가 빈 문자열일 경우 0으로 처리
	        int currentAprvOrder = vo.getAprvOrder().isEmpty() ? 0 : Integer.parseInt(vo.getAprvOrder());

	        // 최대 결재 순서와 비교하여 상태 설정
	        if (currentAprvOrder == maxAprvOrder) {
	            vo.setAprvStatus("완료"); // 마지막 결재자일 경우 '완료'로 변경

	            // 휴가내역에 추가 (해당 코드 로직은 그대로 유지)
	            VacationRequestVO vrVO = new VacationRequestVO();
	            vrVO.setDraftNo(vo.getDraftNo());
	            vacationService.findUsedVacation(vrVO);

	            // 기안자에게 결재 완료 알림 전송
	            ApprovalVO done = approvalService.findTitleEmpNo(vo.getDraftNo());
	            if (done != null) {
	                AlarmVO alarm = new AlarmVO();
	                alarm.setAlarmMessage(done.getTitle() + "의 결재가 완료되었습니다!");
	                alarm.setAlarmType("결재");
	                alarm.setRead("N");
	                alarm.setEmployeeNo(done.getEmployeeNo()); // 기안자
	                alarm.setAlarmIcon("fa-check-circle");

	                alarmService.insertAlarm(alarm);
	            }
	        } else {
	            vo.setAprvStatus("진행"); // 마지막 결재자가 아니면 '진행'으로 설정
	        }

	        // 결재 승인 처리
	        approvalService.processApproval(vo);
	        result.put("success", true);
	    } catch (Exception e) {
	        result.put("success", false);
	        result.put("message", e.getMessage());
	    }
	    return result;
	}



	// 결재페이지 반려처리
	@PostMapping("/aprv/reject")
	@ResponseBody
	public Map<String, Object> reject(@RequestBody AprvRoutesVO vo) {
	    Map<String, Object> result = new HashMap<>();
	    try {
	        approvalService.rejectApproval(vo);
	        
	        // 반려 알림 보내기
	        ApprovalVO back = approvalService.findTitleEmpNo(vo.getDraftNo());

	        if (back != null) {
	            AlarmVO alarm = new AlarmVO();
	            alarm.setAlarmMessage(back.getTitle() + " 문서가 반려되었습니다.");
	            alarm.setAlarmType("결재");
	            alarm.setRead("N");
	            alarm.setEmployeeNo(back.getEmployeeNo());
	            alarm.setAlarmIcon("fa-times-circle");

	            alarmService.insertAlarm(alarm);
	        }
	        
	        result.put("success", true);
	    } catch (Exception e) {
	        result.put("success", false);
	        result.put("message", e.getMessage());
	    }
	    return result;
	}
	
	@GetMapping("/aprv/file/download/{fileId}")
	public ResponseEntity<Resource> downloadFile(@PathVariable int fileId) {
	    AprvFileVO fileVO = aprvFileService.findFileById(fileId);
	    if (fileVO == null) {
	        return ResponseEntity.notFound().build();
	    }

	    String relativePath = fileVO.getFilePath().replaceFirst("/uploads", "");
	    File file = new File(uploadDir + relativePath);

	    if (!file.exists()) {
	        return ResponseEntity.notFound().build();
	    }

	    Resource resource = new FileSystemResource(file);
	    String originalName = fileVO.getFileName();

	    return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_DISPOSITION,
	                    "attachment; filename=\"" + URLEncoder.encode(originalName, StandardCharsets.UTF_8) + "\"")
	            .contentLength(file.length())
	            .contentType(MediaType.APPLICATION_OCTET_STREAM)
	            .body(resource);
	}

	
	@GetMapping("/aprv/vacation")
	@ResponseBody
	public List<VacationVO> getVacationTypes() {
	    EmpVO loginUser = empService.getLoggedInUserInfo();  // 로그인된 사용자 정보
	    VacationVO vctVO = new VacationVO();
	    vctVO.setSuberNo(loginUser.getSuberNo());  // 회사번호 기준으로 조회

	    return vacationService.getAllVacationTypes(vctVO);
	}
	
	@PostMapping("/aprv/deletes")
	public String deleteDraft(@RequestParam("draftNo") int draftNo,
							  @RequestParam("aprvStatus") String aprvStatus,
							  RedirectAttributes redirectAttributes) {
	    try {
	        approvalService.deleteDraft(draftNo);
	        redirectAttributes.addFlashAttribute("message", "문서가 성공적으로 삭제되었습니다.");
	    } catch (Exception e) {
	        e.printStackTrace();
	        redirectAttributes.addFlashAttribute("error", "삭제 중 오류가 발생했습니다.");
	    }
	    
	    String encodedStatus = URLEncoder.encode(aprvStatus, StandardCharsets.UTF_8);
	    return "redirect:/aprv/request?aprvStatus=" + encodedStatus;
	}
	

	@GetMapping("schedulePage")
	public String scheduleList() {
		return "group/schedule/schedule";
	}
	
}
