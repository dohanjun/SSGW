package com.yedam.app.group.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.yedam.app.group.service.FileService;
import com.yedam.app.group.service.PostService;
import com.yedam.app.group.service.RepositoryPostVO;

@Controller
public class RepositoryController {

	private final PostService postService;
	private final FileService fileService;

	public RepositoryController(PostService postService, FileService fileService) {
		this.postService = postService;
		this.fileService = fileService;
	}

	@GetMapping("/totalRepository")
	public String totalRepository() {
		return "group/repository/totalRepository";
	}

	@GetMapping("/departmentRepository")
	public String departmentRepository() {
		return "group/repository/departmentRepository";
	}

	@GetMapping("/individualRepository")
	public String individualRepository() {
		return "group/repository/individualRepository";
	}

	@GetMapping("/detailPost")
	public String detailPost() {
		return "group/repository/detailPost";
	}

//	@PostMapping("/repositoryinsert")
//    public String repositoryInsert() {
//		@RequestParam("title") String title,
//        @RequestParam("writer") String writer,
//        @RequestParam("content") String content,
//        @RequestParam("employeeNo") int employeeNo,
//        @RequestParam("suberNo") int suberNo,
//        @RequestParam(value = "files", required = false) MultipartFile file) {
//
//        // 게시글 객체 생성
//        RepositoryPostVO postVO = new RepositoryPostVO();
//    	postVO.setTitle(title);
//    	postVO.setCotent(content);
//    	postVO.setEmployeeNo(employeeNo);
//
//    	// 게시글 등록 실행
//    	postService.insertPost(postVO);
//
//    	// 파일 업로드 (게시글이 등록된 후)
//        if (file != null && !file.isEmpty()) {
//            fileService.insertFile(postVO.getWritingId(), file);
//        }
//        return "group/repository/repositoryInsert";
//    }

	@GetMapping("/basket")
	public String basket() {
		return "group/repository/basket";
	}

	@GetMapping("/detailBasket")
	public String detailBasket() {
		return "group/repository/detailBasket";
	}

}
