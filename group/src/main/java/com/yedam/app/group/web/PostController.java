package com.yedam.app.group.web;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.EmpVO;
import com.yedam.app.group.service.FileService;
import com.yedam.app.group.service.PostService;
import com.yedam.app.group.service.RepositoryPostVO;
import com.yedam.app.group.service.RepositoryVO;

@Controller
public class PostController {
	private final PostService postService;
    private final FileService fileService;
    private final EmpService empService;

    @Autowired
    public PostController(PostService postService, FileService fileService, EmpService empService) {
        this.postService = postService;
        this.fileService = fileService;
        this.empService = empService;
    }

    // 게시글 등록 폼 (GET)
    @GetMapping("/repositoryInsert")
    public String repositoryInsert(Model model) {
        EmpVO loggedInUser = empService.getLoggedInUserInfo();

        if (loggedInUser == null) {
            throw new IllegalStateException("로그인한 사용자 정보를 찾을 수 없습니다.");
        }

        model.addAttribute("writer", loggedInUser.getEmployeeName()); // 로그인한 사용자 이름
        model.addAttribute("employeeNo", loggedInUser.getEmployeeNo()); // 로그인한 사용자 사원번호

        return "group/repository/repositoryInsert";
    }

    // 게시글 등록 처리 (POST)
    @PostMapping("/insertPost")
    public String insertPost(@ModelAttribute RepositoryPostVO postVO,  
                             @RequestParam(value = "files", required = false) MultipartFile[] files) {
        EmpVO loggedInUser = empService.getLoggedInUserInfo();

        if (loggedInUser == null) {
            throw new IllegalStateException("로그인한 사용자 정보를 찾을 수 없습니다.");
        }
        
        // 작성일자가 null이면 현재 날짜로 설정
        if (postVO.getCreationDate() == null) {
            postVO.setCreationDate(new Date());
        }
        
        System.out.println("전달된 title 값: " + postVO.getTitle());
        System.out.println("전달된 content 값: " + postVO.getContent());
        
        if (files != null && files.length > 0) {
            System.out.println("업로드된 파일 개수: " + files.length);
            for (MultipartFile file : files) {
                System.out.println("파일 이름: " + file.getOriginalFilename() + ", 크기: " + file.getSize());
            }
        } else {
            System.out.println("업로드된 파일 없음.");
        }

        if (postVO.getContent() == null || postVO.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("content 값이 비어 있습니다.");
        }

        RepositoryVO repository;

        if ((loggedInUser.getDepartmentNo() == null || loggedInUser.getDepartmentNo() == 0) &&
        	    (loggedInUser.getEmployeeNo() == 0)) {
        	    // 회사 전체 자료실
        	    repository = postService.getTotalRepository(loggedInUser.getSuberNo());
        	} else if (loggedInUser.getDepartmentNo() != null && loggedInUser.getDepartmentNo() != 0 &&
        	           loggedInUser.getEmployeeNo() == 0) {
        	    // 부서 자료실
        	    repository = postService.getDepartmentRepository(loggedInUser.getSuberNo(), loggedInUser.getDepartmentNo());
        	} else {
        	    // 개인 자료실
        	    repository = postService.getIndividualRepository(loggedInUser.getSuberNo(), loggedInUser.getEmployeeNo());
        	}

        if (repository == null) {
            throw new IllegalStateException("해당 사용자의 자료실을 찾을 수 없습니다.");
        }

        // 게시글 정보 설정
        postVO.setEmployeeNo(loggedInUser.getEmployeeNo());
        postVO.setFileRepositoryId(repository.getFileRepositoryId());

        // 게시글 등록
        Long writingId = postService.insertPost(postVO);

        // 파일 업로드 (게시글이 등록된 후)
        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    fileService.insertFile(writingId, file);
                }
            }
        }

        // 등록한 자료실로 이동 (어느 자료실인지 체크)
        if ((loggedInUser.getDepartmentNo() == null || loggedInUser.getDepartmentNo() == 0) &&
                loggedInUser.getEmployeeNo() == 0) {
                return "redirect:/totalRepository";
            } else if (loggedInUser.getDepartmentNo() != null && loggedInUser.getDepartmentNo() != 0 &&
                       loggedInUser.getEmployeeNo() == 0) {
                return "redirect:/departmentRepository";
            } else {
                return "redirect:/individualRepository";
            }
    }
}
