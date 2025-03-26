package com.yedam.app.group.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.yedam.app.group.mapper.BoardMapper;
import com.yedam.app.group.service.BoardAttachmentVO;
import com.yedam.app.group.service.BoardPostVO;
import com.yedam.app.group.service.BoardService;
import com.yedam.app.group.service.BoardVO;
import com.yedam.app.group.service.EmpVO;

@Service
public class BoardServiceImpl implements BoardService {
	
	private final BoardMapper boardMapper;

    public BoardServiceImpl(BoardMapper boardMapper) {
        this.boardMapper = boardMapper;
    }
	
    @Override
    public List<BoardVO> getBoardsByType(String boardType, int suberNo, Integer departmentNo, Integer employeeNo) {
        return boardMapper.selectBoardsByType(boardType, suberNo, departmentNo, employeeNo);
    }
    
    @Override
    public int getNoticeBoardPostCount(int suberNo, String keyword) {
        return boardMapper.countNoticeBoardPosts(suberNo, keyword);
    }

    @Override
    public List<BoardVO> getNoticeBoardPostsPaged(int suberNo, String keyword, int offset, int limit) {
        return boardMapper.selectNoticeBoardPostsPaged(suberNo, keyword, offset, limit);
    }
    
    @Override
    public List<BoardVO> getDepartmentBoardPostsPaged(Integer suberNo, Integer departmentNo, String keyword, int offset, int limit) {
        return boardMapper.selectDepartmentBoardPostsPaged(suberNo, departmentNo, keyword, offset, limit);
    }

    @Override
    public int getDepartmentBoardPostCount(Integer suberNo, Integer departmentNo, String keyword) {
        return boardMapper.countDepartmentBoardPosts(suberNo, departmentNo, keyword);
    }

    @Override
    public int getFreeBoardPostCount(int suberNo, String keyword) {
        return boardMapper.countFreeBoardPosts(suberNo, keyword);
    }

    @Override
    public List<BoardVO> getFreeBoardPostsPaged(int suberNo, String keyword, int offset, int limit) {
        return boardMapper.selectFreeBoardPostsPaged(suberNo, keyword, offset, limit);
    }
    
    @Override
    public void insertBoardPost(BoardPostVO postVO, String boardType, EmpVO loginUser, List<MultipartFile> files) {
        // 1. board_id 조회
//        Integer boardId = boardMapper.getBoardIdByType(
//            boardType,
//            loginUser.getSuberNo(),
//            boardType.equals("부서") ? loginUser.getDepartmentNo() : null
//        );
//
//        if (boardId == null) {
//            throw new IllegalStateException("해당 게시판이 존재하지 않습니다.");
//        }
//
//        // 2. board_id 설정
//        postVO.setBoardId(boardId);
//        postVO.setLocation(boardType);
//
//        // 3. 게시글 등록
//        boardMapper.insertPost(postVO); // postId 자동 생성됨
//
//        // 4. 파일 저장 처리
//        for (MultipartFile file : files) {
//            if (!file.isEmpty()) {
//                String originalName = file.getOriginalFilename();
//                String uuid = UUID.randomUUID().toString() + "_" + originalName;
//                String savePath = uploadDir + File.separator + uuid;
//
//                try {
//                    file.transferTo(new File(savePath));
//
//                    BoardAttachmentVO attach = new BoardAttachmentVO();
//                    attach.setPostId(postVO.getPostId());
//                    attach.setFilePath("/uploads/" + uuid);  // 웹 접근 경로
//                    attach.setFileTitle(originalName);
//                    attach.setFileRetentionPeriod(new Date());
//
//                    boardMapper.insertAttachment(attach);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    throw new RuntimeException("파일 저장 실패: " + originalName);
//                }
//            }
//        }
    }
}
