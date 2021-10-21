package com.shinvest.ap.socket.mapper;

import java.util.List;
import java.util.Map;

import com.shinvest.ap.app.aprv.model.AprvModel;
import com.shinvest.ap.app.project.model.ProjectModel;
import com.shinvest.ap.common.annotation.ConnMapperFirst;

@ConnMapperFirst
public interface EaiSocketMapper {

	//기본 승인 정보 조회 T_CODE
	List<Map<String,String>> selectDefaultData(String goldwingAprvType) throws Exception;
	
	//보고서 승인 정보 조회
	Map<String,String> selectReportAprvData(String aprvId) throws Exception;
	
	//보고서 역할 승인 정보 조회
	Map<String,String> selectReportRoleAprvData(String aprvId) throws Exception;
	
	//프로젝트 승인 정보 조회
	Map<String,String> selectProjectAprvData(String aprvId) throws Exception;
	
	//프로젝트 야간 사용 승인 정보 조회
	Map<String,String> selectProjectNightUseAprvData(String aprvId) throws Exception;
	
	//사용자 권한 변경 승인 정보 조회
	Map<String,String> selectUserAuthAprvData(String aprvId) throws Exception;
	
	//데이터 권한 승인 정보 조회
	Map<String,String> selectDataAprvData(String aprvId) throws Exception;
	
	//모델 배포 승인 정보 조회
	Map<String,String> selectModelDeployAprvData(String aprvId) throws Exception;
	
	
	
	//승인 상세 Insert(upsert)
	int insertAprvRqstDtl(Map<String,Object> dtlInfo) throws Exception;
	//승인 Update
	int updateAprvRqst(Map<String,Object> aprvInfo) throws Exception;
	//승인 조회
	AprvModel selectAprvRqst(String aprvId) throws Exception;
	
	
	
	//보고서 정보 업데이트
	int updateReport(String aprvId) throws Exception;
	//보고서 이력 등록
	int insertReportHist(String reportId) throws Exception;
	//보고서 사용자 등록
	int insertReportUser(String aprvId) throws Exception;
	//보고서 사용자 이력 등록
	int insertReportUserHist(AprvModel model) throws Exception;
	
	
	
	//보고서 역할 사용자 등록 또는 변경
	int insertReportRoleUser(String aprvId) throws Exception;
	//보고서 역할 사용자 이력 등록
	int insertReportRoleUserHist(AprvModel model) throws Exception;
	//보고서에 역할 정보 업데이트
	int updateReportUserInfo(AprvModel model) throws Exception;
	
	
	
	//프로젝트 승인 정보 업데이트
	int updateProjectApply(AprvModel model) throws Exception;
	//프로젝트 정보 등록 또는 업데이트
	int insertProject(AprvModel model) throws Exception;
	//프로젝트 정보 이력 등록
	int insertProjectHist(AprvModel model) throws Exception;
	//프로젝트 사용자 정보 등록
	int insertProjectUser(AprvModel model) throws Exception;
	//프로젝트 사용자 정보 이력 등록
	int insertProjectUserHist(AprvModel model) throws Exception;
	//프로젝트 조회
	ProjectModel selectProject(String projectId) throws Exception;
	
	
	
	//프로젝트 야간 사용 업데이트
	int updateProjectUserNightUse(Map<String,Object> map) throws Exception;
	//프로젝트 야간 사용 이력 등록
	int insertProjectUserNightUseHist(Map<String,Object> map) throws Exception;
	
	
	
	//사용자 시스템 권한 제거
	int deleteUserSysAuth(String userId) throws Exception;
	//사용자 시스템 권한 변경
	int updateUserSysAuth(Map<String,Object> map) throws Exception;
	//관리자 시스템 권한 제거
	int deleteMgrSysAuth(String userId) throws Exception;
	//관리자 시스템 권한 등록 또는 변경
	int updateMgrSysAuth(Map<String,Object> map) throws Exception;
	//사용자 이력 기록
	int insertUserHist(String userId) throws Exception;
	
	
}
