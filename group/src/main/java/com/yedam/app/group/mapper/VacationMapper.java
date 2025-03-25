package com.yedam.app.group.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yedam.app.group.service.VacationVO;

@Mapper
public interface VacationMapper {
	

    // 전체 사원 조회 (회사 기준)
    public List<VacationVO> getAllEmployeesWithHireDateAndStatus(@Param("suberNo") int suberNo);
    
    // 전체 회사조회
    public List<Integer> getAllSuberNos();

    // 연차 유형 조회 (회사 번호 기준)
    public VacationVO getAnnualVacationType(@Param("suberNo") int suberNo);

    // 연차 내역 존재 여부 (연도 중복 방지용)
    public int existsLeaveHistory(@Param("employeeNo") int employeeNo, @Param("year") String year);
    
    // 연차 내역 수정 (입사일 변경 시 적용용)
    public int updateLeaveHistory(VacationVO vo);

    // 연차 자동 부여 (INSERT)
    public int insertLeaveHistory(VacationVO vo);

    // leave_history_id 시퀀스용 (자동 증가)
    public int getNextLeaveHistoryId();
    
    // 기안문서번호 가져오기 시퀸스용
    public int getNextDraftNo();
    
    
    // 휴가유형 등록
    public int insertVacationType(VacationVO vo);

    // 휴가유형 ID 생성
    public int getNextVacationTypeId();
    
    //
    public List<VacationVO> selectVacationTypeList(Integer suberNo);
    
    //
    public List<VacationVO> selectVacationTypePaging(VacationVO vo);
    
    //
    public int countVacationType(VacationVO vo);
    
}
