package com.yedam.app.group.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yedam.app.group.service.OvertimeVO;

@Mapper
public interface OvertimeMapper {
    
    // ✅ 특정 출퇴근 기록의 초과근무 데이터 조회
    List<OvertimeVO> getOvertimeByWorkAttitudeId(@Param("workAttitudeId") int workAttitudeId);
}
