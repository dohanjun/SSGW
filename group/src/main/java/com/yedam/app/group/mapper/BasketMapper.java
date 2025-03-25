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
    
    BasketVO getBasketDetailById(Long writingId);
    
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
    
    // 전체 자료실 - 관리자
    int countAllBasketPosts(Map<String, Object> map);
    List<BasketVO> selectAllBasketPostsPaged(Map<String, Object> map);

    // 전체 자료실 - 일반 직원
    int countOwnTotalBasketPosts(Map<String, Object> map);
    List<BasketVO> selectOwnTotalBasketPostsPaged(Map<String, Object> map);

    // 부서 자료실
    int countDepartmentBasketPosts(Map<String, Object> map);
    List<BasketVO> selectDepartmentBasketPostsPaged(Map<String, Object> map);

    // 개인 자료실
    int countIndividualBasketPosts(Map<String, Object> map);
    List<BasketVO> selectIndividualBasketPostsPaged(Map<String, Object> map);
    
    void restoreToTotalRepository(Long writingId);
    void restoreToDepartmentRepository(Long writingId);
    void restoreToIndividualRepository(Long writingId);
    
    List<Long> getFileIdsByWritingId(Long writingId); // 🔍 repository_file의 file_id 리스트 조회
    void deleteDownloadLogsByFileId(Long fileId);
    
    void deleteFilesByWritingId(Long writingId);
    int deletePostByWritingId(Long writingId);
    void deleteBasketByWritingId(Long writingId);
}
