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

    // 자료글 등록 폼 (GET)
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

    // 자료글 등록 처리 (POST)
    @PostMapping("/insertPost")
    public String insertPost(@ModelAttribute RepositoryPostVO postVO,
    						 @RequestParam("repositoryType") String repositoryType,
                             @RequestParam(value = "files", required = false) MultipartFile[] files) {
    	EmpVO loggedInUser = empService.getLoggedInUserInfo();

        if (loggedInUser == null) {
            throw new IllegalStateException("로그인한 사용자 정보를 찾을 수 없습니다.");
        }

        // 작성일자 기본값 설정
        if (postVO.getCreationDate() == null) {
            postVO.setCreationDate(new Date());
        }

        if (postVO.getContent() == null || postVO.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("content 값이 비어 있습니다.");
        }

        if (files != null && files.length > 0) {
            System.out.println("업로드된 파일 개수: " + files.length);
            for (MultipartFile file : files) {
                System.out.println("파일 이름: " + file.getOriginalFilename() + ", 크기: " + file.getSize());
            }
        } else {
            System.out.println("업로드된 파일 없음.");
        }

        // repositoryType 값으로 자료실 결정
        RepositoryVO repository = switch (repositoryType) {
            case "전체" -> postService.getTotalRepository(loggedInUser.getSuberNo());
            case "부서" -> postService.getDepartmentRepository(loggedInUser.getSuberNo(), loggedInUser.getDepartmentNo());
            case "개인" -> postService.getIndividualRepository(loggedInUser.getSuberNo(), loggedInUser.getEmployeeNo());
            default -> throw new IllegalArgumentException("올바르지 않은 repositoryType입니다: " + repositoryType);
        };

        if (repository == null) {
            throw new IllegalStateException("해당 사용자의 자료실을 찾을 수 없습니다.");
        }

        postVO.setEmployeeNo(loggedInUser.getEmployeeNo());
        postVO.setFileRepositoryId(repository.getFileRepositoryId());

        Long writingId = postService.insertPost(postVO);

        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    fileService.insertFile(writingId, file);
                }
            }
        }

        // 자료실 종류에 따라 리다이렉트
        return switch (repositoryType) {
            case "전체" -> "redirect:/totalRepository";
            case "부서" -> "redirect:/departmentRepository";
            default -> "redirect:/individualRepository";
        };
    }
}
