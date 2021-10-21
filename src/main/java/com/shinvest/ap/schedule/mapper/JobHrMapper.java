package com.shinvest.ap.schedule.mapper;

import java.util.List;
import java.util.Map;

import com.shinvest.ap.common.annotation.ConnMapperFirst;

@ConnMapperFirst
public interface JobHrMapper {

	//사용자 인터페이스 삭제
	int deleteIfUser() throws Exception;
	//본부 인터페이스 삭제
	int deleteIfHdept() throws Exception;
	//부서 인터페이스 삭제
	int deleteIfDept() throws Exception;
	//직위 인터페이스 삭제
	int deleteIfPstn() throws Exception;

	//사용자 인터페이스 등록
	int insertIfUser(Map<String,List<Map<String,String>>> param) throws Exception;
	//본부 인터페이스 등록
	int insertIfHdept(Map<String,List<Map<String,String>>> param) throws Exception;
	//부서 인터페이스 등록
	int insertIfDept(Map<String,List<Map<String,String>>> param) throws Exception;
	//직위 인터페이스 등록
	int insertIfPstn(Map<String,List<Map<String,String>>> param) throws Exception;
	//사용자 인터페이스 본부 코드 등록
	int updateIfUser() throws Exception;

	//삭제 사용자 이력
	int syncDeleteUserHist() throws Exception;
	//삭제 사용자
	int syncDeleteUser() throws Exception;
	//삭제 사용자 권한 삭제
	int syncDeleteAuth() throws Exception;
	//변경 사용자 이력
	int syncUpdateUserHist() throws Exception;
	//변경 사용자
	int syncUpdateUser() throws Exception;
	//신규 사용자 이력
	int syncInsertUserHist() throws Exception;
	//신규 사용자
	int syncInsertUser() throws Exception;
	//신규 사용자 권한 추가
	int syncInsertAuth() throws Exception;
	//사용자 수정 구분 정리
	int updateModiSeUser() throws Exception;
	//사용자 이전 부서 코드 초기화
	int updateBfDeptCode() throws Exception;
	
	//삭제 본부 이력
	int syncDeleteHdeptHist() throws Exception;
	//삭제 본부
	int syncDeleteHdept() throws Exception;
	//변경 본부 이력
	int syncUpdateHdeptHist() throws Exception;
	//변경 본부
	int syncUpdateHdept() throws Exception;
	//신규 본부 이력
	int syncInsertHdeptHist() throws Exception;
	//신규 본부
	int syncInsertHdept() throws Exception;
	//본부 수정 구분 정리
	int updateModiSeHdept() throws Exception;
	
	//삭제 부서 이력
	int syncDeleteDeptHist() throws Exception;
	//삭제 부서
	int syncDeleteDept() throws Exception;
	//변경 부서 이력
	int syncUpdateDeptHist() throws Exception;
	//변경 부서
	int syncUpdateDept() throws Exception;
	//신규 부서 이력
	int syncInsertDeptHist() throws Exception;
	//신규 부서
	int syncInsertDept() throws Exception;
	//부서 수정 구분 정리
	int updateModiSeDept() throws Exception;
	
	//삭제 직위 이력
	int syncDeletePstnHist() throws Exception;
	//삭제 직위
	int syncDeletePstn() throws Exception;
	//변경 직위 이력
	int syncUpdatePstnHist() throws Exception;
	//변경 직위
	int syncUpdatePstn() throws Exception;
	//신규 직위 이력
	int syncInsertPstnHist() throws Exception;
	//신규 직위
	int syncInsertPstn() throws Exception;
	//직위 수정 구분 정리
	int updateModiSePstn() throws Exception;
}
