package com.yedam.app.group.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yedam.app.group.service.RepositoryPostVO;

@Mapper
public interface PostMapper {
	
	// 게시글 등록
    int insertPost(RepositoryPostVO postVO);

}
