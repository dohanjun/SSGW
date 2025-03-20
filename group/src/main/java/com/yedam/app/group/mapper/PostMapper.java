package com.yedam.app.group.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.yedam.app.group.service.RepositoryPostVO;
import com.yedam.app.group.service.RepositoryVO;

@Mapper
public interface PostMapper {
	
	// 게시글 등록
    void insertPost(RepositoryPostVO postVO);

    // 전체 자료실 조회
    RepositoryVO getTotalRepository(int suberNo);

    // 부서 자료실 조회
    RepositoryVO getDepartmentRepository(int suberNo, int departmentNo);

    // 개인 자료실 조회
    RepositoryVO getIndividualRepository(int suberNo, int employeeNo);
    
    // 전체 자료실 게시글 조회
    List<RepositoryPostVO> getTotalRepositoryPosts(Map<String, Integer> params);

    // 부서 자료실 게시글 조회
    List<RepositoryPostVO> getDepartmentRepositoryPosts(Map<String, Integer> params);

    // 개인 자료실 게시글 조회
    List<RepositoryPostVO> getIndividualRepositoryPosts(Map<String, Integer> params);
    
}
