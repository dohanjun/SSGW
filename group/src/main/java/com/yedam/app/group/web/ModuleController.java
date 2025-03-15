package com.yedam.app.group.web;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yedam.app.group.service.ModuleService;
import com.yedam.app.group.service.ModuleVO;
import com.yedam.app.group.service.SubscriberService;
import com.yedam.app.group.service.SubscriberVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ModuleController {
	
	private final ModuleService moduleService;
	private final SubscriberService subscriberService;
	
	 @GetMapping("/subscribe")
	    public String subscribePage(Model model) {
	        List<ModuleVO> modules = moduleService.getAllModules();
	        model.addAttribute("modules", modules);
	        return "externalPages/subscribePage";
	    }
	 

	 @PostMapping("/save")
	 public ResponseEntity<String> savePayment(@RequestBody Map<String, Object> requestData) {
	     ObjectMapper objectMapper = new ObjectMapper();

	     SubscriberVO subscriber = objectMapper.convertValue(requestData.get("subscriber"), SubscriberVO.class);
	     //PaymentVO payment = objectMapper.convertValue(requestData.get("payment"), PaymentVO.class);
	     //List<ModuleVO> modules = objectMapper.convertValue(requestData.get("modules"), new TypeReference<List<ModuleVO>>() {});

	     System.out.println("구독자 정보: " + subscriber);
	     //System.out.println("결제 정보: " + payment);
	     //System.out.println("선택한 모듈: " + modules);

	     subscriberService.saveSubscriber(subscriber);
	     //paymentService.processPayment(payment);
	     //moduleService.saveModules(modules);

	     return ResponseEntity.ok("구독자, 결제, 모듈 정보 저장 완료");
	 }

}
