package com.yedam.app.group.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.EmpVO;
import com.yedam.app.group.service.ModuleDetailService;
import com.yedam.app.group.service.ModuleService;
import com.yedam.app.group.service.ModuleVO;
import com.yedam.app.group.service.PaymentService;
import com.yedam.app.group.service.PaymentVO;
import com.yedam.app.group.service.SubscriberService;
import com.yedam.app.group.service.SubscriberVO;
import com.yedam.app.group.service.SubscriptionDetailVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ModuleController {

	private final ModuleService moduleService;
	private final SubscriberService subscriberService;
	private final ModuleDetailService moduleDetailService;
	private final PaymentService paymentService;
	private final EmpService empService;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@GetMapping("/subscribe")
	public String subscribePage(Model model) {
		List<ModuleVO> modules = moduleService.getAllModules();
		model.addAttribute("modules", modules);
		return "externalPages/subscribePage";
	}


	@PostMapping("/saveSuber")
	public ResponseEntity<Integer> saveSuber(@RequestBody SubscriberVO subscriber) {
	    subscriber.setSubPw(encoder.encode(subscriber.getSubPw()));
	    int suberNo = subscriberService.saveSubscriber(subscriber);
	    return ResponseEntity.ok(suberNo);
	}

	@PostMapping("/saveUser")
	public ResponseEntity<?> saveUser(@RequestBody EmpVO employee) {   
	    employee.setEmployeePw(encoder.encode(employee.getEmployeePw()));
	    empService.createEmpInfo(employee);
	    return ResponseEntity.ok(employee.getEmployeeNo());
	}



	@PostMapping("/saveSubDetail")
	public ResponseEntity<List<SubscriptionDetailVO>> saveSubDetail(@RequestBody List<SubscriptionDetailVO> request) {
	    List<SubscriptionDetailVO> savedModules = moduleDetailService.saveModuleDetail(request);
	    return ResponseEntity.ok(savedModules);
	}

	@PostMapping("/savePayment")
	public ResponseEntity<PaymentVO> savePayment(@RequestBody PaymentVO payment) {
		paymentService.savePayment(payment);
	    return ResponseEntity.ok(payment);
	}
	
	@PostMapping("/login")
	public String login() {
		return "externalPages/loginPage";
	}
	
	@PostMapping("/insertModule")
	public ResponseEntity<ModuleVO> saveSuber(@RequestBody ModuleVO module) {
		moduleService.addModule(module);
		return ResponseEntity.ok(module);
	}
	
    @PutMapping("/updateModule")
    public ResponseEntity<ModuleVO> updateModule(@RequestBody ModuleVO module) {
        moduleService.updateModule(module);
        return ResponseEntity.ok(module);
    }
    

    @DeleteMapping("/deleteModule/{moduleNo}")
    public ResponseEntity<String> deleteModule(@PathVariable int moduleNo) {
        moduleService.deleteModule(moduleNo);
        return ResponseEntity.ok("삭제 완료");
    }
    
    @PatchMapping("/updateModuleBasic/{moduleNo}")
    public ResponseEntity<String> updateModuleBasic(@PathVariable int moduleNo) {
        moduleService.updateModuleBasic(moduleNo);
        return ResponseEntity.ok("변환 완료");
    }
    
    @PatchMapping("/updateModuleActive/{moduleNo}")
    public ResponseEntity<String> updateModuleActive(@PathVariable int moduleNo) {
        moduleService.updateModuleActive(moduleNo);
        return ResponseEntity.ok("변환 완료");
    }
    
	//비밀번호 변경

	@PostMapping("/updatePassword")
	@ResponseBody
	public ResponseEntity<?> resetPassword(@RequestBody SubscriberVO subscriber) {
	    
	    // 비밀번호 암호화
	    String encodedPw = encoder.encode(subscriber.getSubPw());
	    subscriber.setSubPw(encodedPw);
	    
	    // Subscriber 테이블 비밀번호 업데이트
	    subscriberService.updateSubscriberPassword(subscriber);

	    // Emp 테이블 비밀번호 업데이트
	    EmpVO employee = new EmpVO();
	    employee.setSuberNo(subscriber.getSuberNo());
	    employee.setEmployeePw(encodedPw);
	    empService.updateEmployeePasswordBySuberNo(employee);

	    return ResponseEntity.ok().build();
	}
}
