package com.yedam.app.group.web;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

	@GetMapping("/subscribe")
	public String subscribePage(Model model) {
		List<ModuleVO> modules = moduleService.getAllModules();
		model.addAttribute("modules", modules);
		return "externalPages/subscribePage";
	}

	@PostMapping("/saveSuber")
	public ResponseEntity<Integer> saveSuber(@RequestBody SubscriberVO subscriber) {
		int suberNo = subscriberService.saveSubscriber(subscriber);
		return ResponseEntity.ok(suberNo);
	}

	@PostMapping("/saveSubDetail")
	public ResponseEntity<String> saveSubDetail(@RequestBody List<SubscriptionDetailVO> request) {
		List<SubscriptionDetailVO> modules = request;
		moduleDetailService.saveModuleDetail(modules);
		return ResponseEntity.ok("모듈 저장 성공");
	}

	@PostMapping("/savePayment")
	public ResponseEntity<String> savePayment(@RequestBody PaymentVO payment) {
		System.out.println("결제내역"+payment);
		paymentService.savePayment(payment);
		return ResponseEntity.ok("내역 저장 성공");
	}
	
	@PostMapping("/login")
	public String login() {
		return "externalPages/loginPage";
	}
}
