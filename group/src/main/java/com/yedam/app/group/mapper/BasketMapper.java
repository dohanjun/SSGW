package com.yedam.app.group.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yedam.app.group.service.BasketVO;

@Mapper
public interface BasketMapper {
	void insertToBasket(Long writingId);
	
	// 30일 지난 writing_id 리스트 조회
    List<Long> selectExpiredBasketWritingIds();
	
    List<BasketVO> selectAllFromBasket();
    
    BasketVO selectBasketByWritingId(Long writingId);
    
    // writingId로 자료실 유형 조회
    String getRepositoryTypeByWritingId(Long writingId);

    // 자료실 유형 포함 휴지통 등록
    void insertToBasketWithType(@Param("writingId") Long writingId, @Param("repositoryType") String repositoryType);
    
    List<BasketVO> selectAllFromBasketBySuber(@Param("suberNo") Integer suberNo);

    List<BasketVO> selectBasketByTypeFiltered(Map<String, Object> params);
    
    List<BasketVO> selectOwnTotalBasketPosts(int suberNo, int employeeNo);
    
    // 부서 휴지통 (작성자 or 부서장만 접근 가능하도록 파라미터 하나로 처리)
    List<BasketVO> selectDepartmentBasketFiltered(Map<String, Object> params);
    
    List<BasketVO> selectIndividualBasket(Map<String, Object> params);
    
    void restoreToTotalRepository(Long writingId);
    void restoreToDepartmentRepository(Long writingId);
    void restoreToIndividualRepository(Long writingId);
    
    void deleteFilesByWritingId(Long writingId);
    void deletePostByWritingId(Long writingId);
    void deleteBasketByWritingId(Long writingId);
}
