package com.yedam.app.group.web;

import java.util.List;

import org.springframework.stereotype.Controller;

import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.EmpVO;
import com.yedam.app.group.service.SubscriberService;
import com.yedam.app.group.service.SubscriberVO;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CompanyManagementsController {

    private final EmpService empService;
    private final SubscriberService subscriberService;

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
	    model.addAttribute("moduleList", suber);
	    return "group/managements/moduleManagementPage";
	}
}

