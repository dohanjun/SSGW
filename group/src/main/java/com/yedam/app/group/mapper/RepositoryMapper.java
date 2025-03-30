package com.yedam.app.group.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yedam.app.group.service.RepositoryPostVO;
import com.yedam.app.group.service.RepositoryVO;

@Mapper
public interface RepositoryMapper {
	RepositoryVO findTotalRepository(@Param("suberNo") int suberNo);
    RepositoryVO findDepartmentRepository(@Param("suberNo") int suberNo, @Param("departmentNo") int departmentNo);
    RepositoryVO findIndividualRepository(@Param("suberNo") int suberNo, @Param("employeeNo") int employeeNo);
    
    List<RepositoryPostVO> getRecentRepositoryPosts();
}
