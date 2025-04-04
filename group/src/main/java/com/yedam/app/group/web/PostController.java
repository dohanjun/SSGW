package com.yedam.app.group.web;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yedam.app.group.service.EmpService;
import com.yedam.app.group.service.EmpVO;
import com.yedam.app.group.service.FileService;
import com.yedam.app.group.service.PostService;
import com.yedam.app.group.service.RepositoryFileVO;
import com.yedam.app.group.service.RepositoryPostVO;
import com.yedam.app.group.service.RepositoryVO;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class PostController {
	private final PostService postService;
    private final FileService fileService;
    private final EmpService empService;

    public PostController(PostService postService, FileService fileService, EmpService empService) {
        this.postService = postService;
        this.fileService = fileService;
        this.empService = empService;
    }

    // 자료글 등록 폼 (GET)
    @GetMapping("/repositoryInsert")
    public String repositoryInsert(@RequestParam("repositoryType") String repositoryType, Model model) {
        EmpVO loggedInUser = empService.getLoggedInUserInfo();

        model.addAttribute("writer", loggedInUser.getEmployeeName()); // 로그인한 사용자 이름
        model.addAttribute("employeeNo", loggedInUser.getEmployeeNo()); // 로그인한 사용자 사원번호
        model.addAttribute("repositoryType", repositoryType);
        return "group/repository/repositoryInsert";
    }

    // 자료글 등록 처리 (POST)
    @PostMapping("/insertPost")
    public String insertPost(@ModelAttribute RepositoryPostVO postVO,
    						 @RequestParam("repositoryType") String repositoryType,
                             @RequestParam(value = "files", required = false) MultipartFile[] files,
                             HttpServletRequest request) {
    	EmpVO loggedInUser = empService.getLoggedInUserInfo();

        // 작성일자 기본값 설정
        if (postVO.getCreationDate() == null) {
            postVO.setCreationDate(new Date());
        }

        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
            }
        } else {
        }

        // repositoryType 값으로 자료실 결정
        RepositoryVO repository = switch (repositoryType) {
            case "전체" -> postService.getTotalRepository(loggedInUser.getSuberNo());
            case "부서" -> postService.getDepartmentRepository(loggedInUser.getSuberNo(), loggedInUser.getDepartmentNo());
            case "개인" -> postService.getIndividualRepository(loggedInUser.getSuberNo(), loggedInUser.getEmployeeNo());
            default -> throw new IllegalArgumentException("올바르지 않은 repositoryType입니다: " + repositoryType);
        };

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
            case "전체" -> "redirect:/totalRepository?page=1";
            case "부서" -> "redirect:/departmentRepository?page=1";
            case "개인" -> "redirect:/individualRepository?page=1";
            default -> throw new IllegalArgumentException("올바르지 않은 repositoryType입니다: " + repositoryType);
        };
    }
    
    @GetMapping("/editPost")
    public String editPostForm(@RequestParam("writingId") Long writingId, Model model) {
        RepositoryPostVO post = postService.getPostDetail(writingId);
        List<RepositoryFileVO> files = fileService.getFilesByWritingId(writingId);
        
        model.addAttribute("post", post);
        model.addAttribute("files", files);
        return "group/repository/editPost";
    }
    
    @PostMapping("/editPost")
    public String updatePost(@ModelAttribute RepositoryPostVO postVO,
                             @RequestParam(value = "files", required = false) MultipartFile[] files,
                             @RequestParam(value = "deleteFileIds", required = false) List<Long> deleteFileIds) {

        // 게시글 제목/내용 수정
        postService.updatePost(postVO);

        // 기존 파일 삭제 전 다운로드 로그 먼저 삭제!
        fileService.deleteDownloadLogByWritingId(postVO.getWritingId());

        // 체크된 파일만 삭제
        if (deleteFileIds != null && !deleteFileIds.isEmpty()) {
            for (Long fileId : deleteFileIds) {
                fileService.deleteFileById(fileId);
            }
        }

        // 새 파일 업로드
        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    fileService.insertFile(postVO.getWritingId(), file);
                }
            }
        }

        return "redirect:/detailPost/" + postVO.getWritingId();
    }
    
    @PostMapping("/toggleFix")
    public String toggleFix(@RequestParam("writingId") Long writingId, RedirectAttributes redirectAttributes) {
        RepositoryPostVO post = postService.getPostDetail(writingId);
        String repoType = post.getRepositoryType();

        // 개인 자료실은 고정 기능 없음
        if ("개인".equals(repoType)) {
            return "redirect:/individualRepository";
        }

        char newFix = (post.getFix() == 'Y') ? 'N' : 'Y';

        // 로그인한 유저에서 회사 번호 가져오기
        EmpVO loggedInUser = empService.getLoggedInUserInfo();
        Integer suberNo = loggedInUser.getSuberNo();

        // 고정 시도일 경우 → 고정글 개수 검사
        if (newFix == 'Y' && postService.countFixedPosts(suberNo, repoType) >= 5) {
            redirectAttributes.addFlashAttribute("errorMessage", "고정글은 최대 5개까지만 가능합니다.");
            return "redirect:/detailPost/" + writingId;  // 상세페이지로 redirect
        }

        postService.updateFixStatus(writingId, newFix);
        return "redirect:" + getRepositoryRedirectUrl(repoType);
    }

    private String getRepositoryRedirectUrl(String repoType) {
        switch (repoType) {
            case "전체": return "/totalRepository";
            case "부서": return "/departmentRepository";
            default: return "/";
        }
    }
}
