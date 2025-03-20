package com.yedam.app.group.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.yedam.app.group.service.RepositoryPostVO;
import com.yedam.app.group.service.RepositoryVO;

@Mapper
public interface PostMapper {
	
	// 게시글 등록
    int insertPost(RepositoryPostVO postVO);

    // 전체 자료실 조회
    RepositoryVO getTotalRepository(int suberNo);

    // 부서 자료실 조회
    RepositoryVO getDepartmentRepository(int suberNo, int departmentNo);

    // 개인 자료실 조회
    RepositoryVO getIndividualRepository(int suberNo, int employeeNo);
    
    List<RepositoryPostVO> getTotalRepositoryPosts(int fileRepositoryId);
    
}
