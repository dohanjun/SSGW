package com.yedam.app.group.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.yedam.app.group.service.GroupVO;
import com.yedam.app.group.service.MailVO;
@Mapper
public interface GroupMapper {
	public List<GroupVO> selectAllList();
}
