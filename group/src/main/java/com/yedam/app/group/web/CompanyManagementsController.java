package com.yedam.app.group.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.EmpVO;
import com.yedam.app.group.service.ModuleService;
import com.yedam.app.group.service.ModuleVO;
import com.yedam.app.group.service.PaymentService;
import com.yedam.app.group.service.PaymentVO;
import com.yedam.app.group.service.SubscriberService;
import com.yedam.app.group.service.SubscriberVO;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CompanyManagementsController {

    private final EmpService empService;
    private final SubscriberService subscriberService;
    private final PaymentService paymentService;
    private final ModuleService moduleService;
 

    @GetMapping("/companyManagements")
    public String companyPage(Model model) {
        EmpVO loggedInUser = empService.getLoggedInUserInfo();
        int suberNo = loggedInUser.getSuberNo();
        model.addAttribute("suber", subscriberService.findinfoSuber(suberNo));
        return "group/managements/companyManagementPage";
    }
    
	@GetMapping("/suberModuleInfo")
	public String suberModuleInfoPage(@RequestParam int suberNo, Model model) {
		List<SubscriberVO> suber = subscriberService.findinfoSuberByNo(suberNo);
		EmpVO loggedInUser = empService.getLoggedInUserInfo();
	    model.addAttribute("moduleList", suber);
	    model.addAttribute("suber", loggedInUser);
	    return "group/managements/moduleManagementPage";
	}
	
	@PostMapping("/paymentInfoManagement")
	public ResponseEntity<Map<String, Object>> selectAllpayMentDetail(@RequestParam int suberNo) {
	    List<PaymentVO> childPost = paymentService.findAllpayMent(suberNo);
	    SubscriberVO suberInfo = subscriberService.findinfoSuber(suberNo);

	    Map<String, Object> result = new HashMap<>();
	    result.put("paymentList", childPost);
	    result.put("suberInfo", suberInfo);

	    return ResponseEntity.ok(result);
	}
	
	@GetMapping("/moduleList")
	public ResponseEntity<List<ModuleVO>> moduleList() {
	    List<ModuleVO> modules = moduleService.getAllModules();
	    return ResponseEntity.ok(modules);
	}
	
	@PostMapping("/checkSubscribedModules")
	@ResponseBody
	public Map<String, Object> checkSubscribedModules(@RequestBody Map<String, Object> data) {
	    int suberNo = (int) data.get("suberNo");
	    List<Integer> moduleNos = ((List<?>) data.get("moduleNos"))
	    	    .stream()
	    	    .map(obj -> Integer.parseInt(obj.toString()))
	    	    .collect(Collectors.toList());

	    List<Integer> alreadySubscribed = moduleService.findSubscribedModuleNos(suberNo, moduleNos); // 중복 조회 메서드
	    
	    Map<String, Object> result = new HashMap<>();
	    if (!alreadySubscribed.isEmpty()) {
	        result.put("status", "duplicated");
	        result.put("duplicatedModules", alreadySubscribed);
	    } else {
	        result.put("status", "ok");
	    }
	    return result;
	}
	
	@PostMapping("/updateSuber")
	@ResponseBody
	public ResponseEntity<String> updateSuber(@RequestBody SubscriberVO vo) {
			subscriberService.updateSuber(vo);
	        return ResponseEntity.ok("구독자 정보가 성공적으로 수정되었습니다.");
	}
}

