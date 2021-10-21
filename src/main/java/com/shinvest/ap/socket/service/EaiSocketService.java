package com.shinvest.ap.socket.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.shinvest.ap.app.aprv.model.AprvModel;
import com.shinvest.ap.app.project.model.ProjectModel;
import com.shinvest.ap.common.Constant;
import com.shinvest.ap.common.aws.AwsLambdaUtil;
import com.shinvest.ap.socket.EaiSocketNotifyClient;
import com.shinvest.ap.socket.mapper.EaiSocketMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EaiSocketService {

	@Resource(name="eaiSocketMapper")
	private EaiSocketMapper mapper;
	
	@Resource(name="eaiSocketNotifyClient")
	private EaiSocketNotifyClient eaiSocketNotifyClient;
	
	@Resource(name="awsLambdaUtil")
	private AwsLambdaUtil awsLambdaUtil;
	
	/**
	 * 결재 쪽지 알림 발송
	 * @param data
	 */
	public void requestNotify(JSONObject data) {
		
		//eaiSocketNotifyClient.requestNotify(receiverIds, senderId, senderNm, message, link);
		
	}
	
	/**
	 * 기본 에러 메시지
	 */
	public void setErrorMsg(JSONObject data) {
		data.put(Constant.SocketServer.RESULT, Constant.SocketServer.ERROR);
		data.put(Constant.SocketServer.ERROR_REASON, Constant.SocketServer.ERROR_MSG);
		return;
	}
	
	
	/**
	 * 보고서 승인 DRAFTINIT
	 * @param data
	 * @return
	 */
	public JSONObject reportAprvFirst(JSONObject data) {
		JSONObject result = new JSONObject();
		//DB 조회 후 HTML 구성
		//반환 JSONObject
		//  Constant.SocketServer.RESULT    : 결과 / 성공 - "OK" / 실패 - "ERROR"
		//  Constant.SocketServer.ERROR_MSG : 오류설명 / 처리 실패인 경우 "전자결재 처리 중 오류가 발생하였습니다."
		//  Constant.SocketServer.HTML_DATA : 전자결재문서내용 HTML
		//  Constant.SocketServer.TITLE     : 전자결재제목   / "BI 보고서 게시(등록) 요청"
		//  Constant.SocketServer.SUB_TITLE : 전자결재부제목 / "BI 보고서 게시(등록) 요청"
		//  Constant.SocketServer.RCV_DEPT_CODE : 수신조직ID / "GS713"
		//  Constant.SocketServer.RCV_DEPT_NM   : 수신조직명 / "데이터사이언트팀"
		
		if (StringUtils.isNotBlank(data.optString(Constant.SocketServer.APRV_ID))) {
			try {
				//승인 기본 정보 조회
				List<Map<String,String>> aprvDefaultData = mapper.selectDefaultData(Constant.GoldwingAprvType.REPORT_OPEN);
				for (Map<String,String> map : aprvDefaultData) {
					result.put(map.get("code_id"), map.get("code_nm"));
				}
				
				//승인 정보 조회
				Map<String,String> aprvData = mapper.selectReportAprvData(data.optString(Constant.SocketServer.APRV_ID));
				StringBuilder htmlData = new StringBuilder();
				htmlData.append("<table style='TABLE-LAYOUT:fixed;border-spacing:0px;'border=1>");
				htmlData.append("<colgroup><col style='width:25%' /><col style='width:25%' /><col style='width:25%' /><col style='width:25%' /></colgroup>");
				htmlData.append("<tbody><tr><th colspan='4'>신청자 정보</th></tr>");
				htmlData.append("<tr><th>성명</th><td>").append(StringUtils.defaultIfBlank(aprvData.get("user_nm"),"")).append("</td>");
				htmlData.append("<th>사번</th><td>").append(StringUtils.defaultIfBlank(aprvData.get("user_id"),"")).append("</td></tr>");
				htmlData.append("<tr><th>부서</th><td>").append(StringUtils.defaultIfBlank(aprvData.get("dept_nm"),"")).append("</td>");
				htmlData.append("<th>직위</th><td>").append(StringUtils.defaultIfBlank(aprvData.get("pstn_nm"),"")).append("</td></tr>");
				htmlData.append("<tr><th>요청 사유</th><td colspan='3'>").append(StringUtils.defaultIfBlank(aprvData.get("rqst_resn"),"")).append("</td></tr>");
				htmlData.append("<tr><th>프로젝트명</th><td colspan='3'>").append(StringUtils.defaultIfBlank(aprvData.get("project_nm"),"")).append("</td></tr>");
				htmlData.append("<tr><th>보고서명</th><td colspan='3'>").append(StringUtils.defaultIfBlank(aprvData.get("report_nm"),"")).append("</td></tr>");
				htmlData.append("<tr><th>보고서 URL 정보</th><td colspan='3'>").append(StringUtils.defaultIfBlank(aprvData.get("report_url"),"")).append("</td></tr>");
				htmlData.append("<tr><th>보고서 유형</th><td colspan='3'>").append(StringUtils.defaultIfBlank(aprvData.get("report_ty"),"")).append("</td></tr>");
				htmlData.append("<tr><th>보고서 관리 부서</th><td colspan='3'>").append(StringUtils.defaultIfBlank(aprvData.get("dept_info"),"")).append("</td></tr>");
				htmlData.append("<tr><th>보고서 담당자</th><td colspan='3'>").append(StringUtils.defaultIfBlank(aprvData.get("picr_info"),"")).append("</td></tr>");
				htmlData.append("<tr><th>보기 권한 신청자</th><td colspan='3'>").append(StringUtils.defaultIfBlank(aprvData.get("pcpt_info"),"")).append("</td></tr>");
				htmlData.append("<tr><th>편집 권한 신청자</th><td colspan='3'>").append(StringUtils.defaultIfBlank(aprvData.get("mgr_info"),"")).append("</td></tr>");
				htmlData.append("</tbody></table><p style='color:red'>* 보고서 썸네일 필수 첨부</p>");
				result.put(Constant.SocketServer.HTML_DATA, htmlData);
				
				result.put(Constant.SocketServer.RESULT, Constant.SocketServer.SUCCESS);
			} catch (Exception e) {
				log.warn("EAI Socket Sever 보고서 승인 DRAFTINIT 처리 중 오류");
				log.warn(e.getMessage());
			}
		}
		
		if (StringUtils.isBlank(result.optString(Constant.SocketServer.RESULT))) {
			setErrorMsg(result);
		}
		return result;
	}
	
	/**
	 * 보고서 승인 DRAFTSTART, DRAFTPROC, DRAFTEND, ...
	 * @param data
	 * @return
	 */
	@Transactional
	public JSONObject reportAprv(JSONObject data) {
		JSONObject result = new JSONObject();
		// 승인 정보 업데이트 및 후처리
		//반환 JSONObject
		//  Constant.SocketServer.RESULT    : 결과 / 성공 - "OK" / 실패 - "ERROR"
		//  Constant.SocketServer.ERROR_MSG : 오류설명 / 처리 실패인 경우 "전자결재 처리 중 오류가 발생하였습니다."
		//  Constant.SocketServer.HTML_DATA : 전자결재문서내용 HTML / NULL
		//  Constant.SocketServer.TITLE     : 전자결재제목   / NULL
		//  Constant.SocketServer.SUB_TITLE : 전자결재부제목 / NULL
		//  Constant.SocketServer.RCV_DEPT_CODE : 수신조직ID / NULL
		//  Constant.SocketServer.RCV_DEPT_NM   : 수신조직명 / NULL

		try {
			// 상세 등록 및 수정
			JSONArray aprvDtlInfo = data.getJSONArray(Constant.SocketServer.APRV_DTL_INFO);
			for (int i=0; i<aprvDtlInfo.length(); i++) {
				Map<String,Object> dtlInfo = aprvDtlInfo.getJSONObject(i).toMap();
				mapper.insertAprvRqstDtl(dtlInfo);
			}
			Map<String,Object> aprvInfo = data.toMap();
			if (StringUtils.equals(Constant.SocketServer.STAT_APRV,data.optString(Constant.SocketServer.APRV_STAT))) {
				//승인 처리
				mapper.updateAprvRqst(aprvInfo);
				// 후처리 : 보고서 정보 업데이트 및 이력 기록
				//승인 정보 조회
				AprvModel aprv = mapper.selectAprvRqst(data.getString(Constant.SocketServer.APRV_ID));
				// 보고서 정보 업데이트
				mapper.updateReport(aprv.getAprvId());
				// 보고서 사용자 등록
				mapper.insertReportUser(aprv.getAprvId());
				// 보고서 이력 등록
				mapper.insertReportHist(aprv.getRefId());
				// 보고서 사용자 이력 등록
				mapper.insertReportUserHist(aprv);
			} else if (StringUtils.equals(Constant.SocketServer.STAT_REJECT,data.optString(Constant.SocketServer.APRV_STAT))) {
				//반려 처리
				mapper.updateAprvRqst(aprvInfo);
				// 후처리 없음
			} else {
				//결재중 처리
				// 후처리 없음
			}
			result.put(Constant.SocketServer.RESULT, Constant.SocketServer.SUCCESS);
		} catch (Exception e) {
			log.warn("EAI Socket Sever 보고서 승인 처리 중 오류");
			log.warn(e.getMessage());
		}
		
		if (StringUtils.isBlank(result.optString(Constant.SocketServer.RESULT))) {
			setErrorMsg(result);
		}
		return result;
	}
	
	/**
	 * 보고서 역할 승인 - 사용자 승인 DRAFTINIT
	 * @param data
	 * @return
	 */
	public JSONObject reportRoleAprvFirst(JSONObject data) {
		JSONObject result = new JSONObject();
		//반환 JSONObject
		//  Constant.SocketServer.RESULT    : 결과 / 성공 - "OK" / 실패 - "ERROR"
		//  Constant.SocketServer.ERROR_MSG : 오류설명 / 처리 실패인 경우 "전자결재 처리 중 오류가 발생하였습니다."
		//  Constant.SocketServer.HTML_DATA : 전자결재문서내용 HTML
		//  Constant.SocketServer.TITLE     : 전자결재제목   / "BI 보고서 권한 요청"
		//  Constant.SocketServer.SUB_TITLE : 전자결재부제목 / "BI 보고서 권한 요청"
		//  Constant.SocketServer.RCV_DEPT_CODE : 수신조직ID / "GS713"
		//  Constant.SocketServer.RCV_DEPT_NM   : 수신조직명 / "데이터사이언트팀"
		
		if (StringUtils.isNotBlank(data.optString(Constant.SocketServer.APRV_ID))) {
			try {
				//승인 기본 정보 조회
				List<Map<String,String>> aprvDefaultData = mapper.selectDefaultData(Constant.GoldwingAprvType.REPORT_ROLE);
				for (Map<String,String> map : aprvDefaultData) {
					result.put(map.get("code_id"), map.get("code_nm"));
				}
				
				//승인 정보 조회
				Map<String,String> aprvData = mapper.selectReportRoleAprvData(data.optString(Constant.SocketServer.APRV_ID));
				StringBuilder htmlData = new StringBuilder();
				htmlData.append("<table style='TABLE-LAYOUT:fixed;border-spacing:0px;'border=1>");
				htmlData.append("<colgroup><col style='width:25%' /><col style='width:25%' /><col style='width:25%' /><col style='width:25%' /></colgroup>");
				htmlData.append("<tbody><tr><th colspan='4'>신청자 정보</th></tr>");
				htmlData.append("<tr><th>성명</th><td>").append(StringUtils.defaultIfBlank(aprvData.get("user_nm"),"")).append("</td>");
				htmlData.append("<th>사번</th><td>").append(StringUtils.defaultIfBlank(aprvData.get("user_id"),"")).append("</td></tr>");
				htmlData.append("<tr><th>부서</th><td>").append(StringUtils.defaultIfBlank(aprvData.get("dept_nm"),"")).append("</td>");
				htmlData.append("<th>직위</th><td>").append(StringUtils.defaultIfBlank(aprvData.get("pstn_nm"),"")).append("</td></tr>");
				htmlData.append("<tr><th>요청 사유</th><td colspan='3'>").append(StringUtils.defaultIfBlank(aprvData.get("rqst_resn"),"")).append("</td></tr>");
				htmlData.append("<tr><th>프로젝트명</th><td colspan='3'>").append(StringUtils.defaultIfBlank(aprvData.get("project_nm"),"")).append("</td></tr>");
				htmlData.append("<tr><th>보고서명</th><td colspan='3'>").append(StringUtils.defaultIfBlank(aprvData.get("report_nm"),"")).append("</td></tr>");
				htmlData.append("<tr><th>보고서 URL 정보</th><td colspan='3'>").append(StringUtils.defaultIfBlank(aprvData.get("report_url"),"")).append("</td></tr>");
				htmlData.append("<tr><th>보고서 유형</th><td colspan='3'>").append(StringUtils.defaultIfBlank(aprvData.get("report_ty"),"")).append("</td></tr>");
				htmlData.append("<tr><th>보고서 관리 부서</th><td colspan='3'>").append(StringUtils.defaultIfBlank(aprvData.get("dept_info"),"")).append("</td></tr>");
				htmlData.append("<tr><th>보고서 담당자</th><td colspan='3'>").append(StringUtils.defaultIfBlank(aprvData.get("picr_info"),"")).append("</td></tr>");
				htmlData.append("<tr><th>보기 권한 신청자</th><td colspan='3'>").append(StringUtils.defaultIfBlank(aprvData.get("pcpt_info"),"")).append("</td></tr>");
				htmlData.append("<tr><th>편집 권한 신청자</th><td colspan='3'>").append(StringUtils.defaultIfBlank(aprvData.get("mgr_info"),"")).append("</td></tr>");
				htmlData.append("</tbody></table>");
				result.put(Constant.SocketServer.HTML_DATA, htmlData);
				
				result.put(Constant.SocketServer.RESULT, Constant.SocketServer.SUCCESS);
			} catch (Exception e) {
				log.warn("EAI Socket Sever 보고서 역할 승인 DRAFTINIT 처리 중 오류");
				log.warn(e.getMessage());
			}
		}
		
		if (StringUtils.isBlank(result.optString(Constant.SocketServer.RESULT))) {
			setErrorMsg(result);
		}
		return result;
	}
	
	/**
	 * 보고서 역할 승인 - 사용자 승인 DRAFTSTART, DRAFTPROC, DRAFTEND, ...
	 * @param data
	 * @return
	 */
	@Transactional
	public JSONObject reportRoleAprv(JSONObject data) {
		JSONObject result = new JSONObject();
		// 승인 정보 업데이트 및 후처리
		//반환 JSONObject
		//  Constant.SocketServer.RESULT    : 결과 / 성공 - "OK" / 실패 - "ERROR"
		//  Constant.SocketServer.ERROR_MSG : 오류설명 / 처리 실패인 경우 "전자결재 처리 중 오류가 발생하였습니다."
		//  Constant.SocketServer.HTML_DATA : 전자결재문서내용 HTML / NULL
		//  Constant.SocketServer.TITLE     : 전자결재제목   / NULL
		//  Constant.SocketServer.SUB_TITLE : 전자결재부제목 / NULL
		//  Constant.SocketServer.RCV_DEPT_CODE : 수신조직ID / NULL
		//  Constant.SocketServer.RCV_DEPT_NM   : 수신조직명 / NULL

		try {
			// 상세 등록 및 수정
			JSONArray aprvDtlInfo = data.getJSONArray(Constant.SocketServer.APRV_DTL_INFO);
			for (int i=0; i<aprvDtlInfo.length(); i++) {
				Map<String,Object> dtlInfo = aprvDtlInfo.getJSONObject(i).toMap();
				mapper.insertAprvRqstDtl(dtlInfo);
			}
			Map<String,Object> aprvInfo = data.toMap();
			if (StringUtils.equals(Constant.SocketServer.STAT_APRV,data.optString(Constant.SocketServer.APRV_STAT))) {
				//승인 처리
				mapper.updateAprvRqst(aprvInfo);
				// 후처리 : 보고서 역할 정보 업데이트 및 이력 기록
				//승인 정보 조회
				AprvModel aprv = mapper.selectAprvRqst(data.getString(Constant.SocketServer.APRV_ID));
				// 보고서 역할 정보 등록 또는 업데이트
				mapper.insertReportRoleUser(aprv.getAprvId());
				// 보고서 역할 정보 이력
				mapper.insertReportRoleUserHist(aprv);
				// 보고서에 역할 정보 업데이트
				mapper.updateReportUserInfo(aprv);
				// 보고서 이력 등록
				mapper.insertReportHist(aprv.getRefId());
			} else if (StringUtils.equals(Constant.SocketServer.STAT_REJECT,data.optString(Constant.SocketServer.APRV_STAT))) {
				//반려 처리
				mapper.updateAprvRqst(aprvInfo);
				// 후처리 없음
			} else {
				//결재중 처리
				// 후처리 없음
			}
			result.put(Constant.SocketServer.RESULT, Constant.SocketServer.SUCCESS);
		} catch (Exception e) {
			log.warn("EAI Socket Sever 보고서 역할 승인 처리 중 오류");
			log.warn(e.getMessage());
		}
		
		if (StringUtils.isBlank(result.optString(Constant.SocketServer.RESULT))) {
			setErrorMsg(result);
		}
		return result;
	}
	
	/**
	 * 프로젝트 승인 DRAFTINIT
	 * @param data
	 * @return
	 */
	public JSONObject projectAprvFirst(JSONObject data) {
		JSONObject result = new JSONObject();
		//DB 조회 후 HTML 구성
		//반환 JSONObject
		//  Constant.SocketServer.RESULT    : 결과 / 성공 - "OK" / 실패 - "ERROR"
		//  Constant.SocketServer.ERROR_MSG : 오류설명 / 처리 실패인 경우 "전자결재 처리 중 오류가 발생하였습니다."
		//  Constant.SocketServer.HTML_DATA : 전자결재문서내용 HTML
		//  Constant.SocketServer.TITLE     : 전자결재제목   / "프로젝트 등록/수정(sandbox 자원요청)"
		//  Constant.SocketServer.SUB_TITLE : 전자결재부제목 / "프로젝트 등록/수정(sandbox 자원요청)"
		//  Constant.SocketServer.RCV_DEPT_CODE : 수신조직ID / "GS713"
		//  Constant.SocketServer.RCV_DEPT_NM   : 수신조직명 / "데이터사이언트팀"
		
		if (StringUtils.isNotBlank(data.optString(Constant.SocketServer.APRV_ID))) {
			try {
				//승인 기본 정보 조회
				List<Map<String,String>> aprvDefaultData = mapper.selectDefaultData(Constant.GoldwingAprvType.PROJECT);
				for (Map<String,String> map : aprvDefaultData) {
					result.put(map.get("code_id"), map.get("code_nm"));
				}
				
				//승인 정보 조회
				Map<String,String> aprvData = mapper.selectProjectAprvData(data.optString(Constant.SocketServer.APRV_ID));
				StringBuilder htmlData = new StringBuilder();
				htmlData.append("<table style='TABLE-LAYOUT:fixed;border-spacing:0px;'border=1>");
				htmlData.append("<colgroup><col style='width:25%' /><col style='width:25%' /><col style='width:25%' /><col style='width:25%' /></colgroup>");
				htmlData.append("<tbody><tr><th colspan='4'>신청자 정보</th></tr>");
				htmlData.append("<tr><th>성명</th><td>").append(StringUtils.defaultIfBlank(aprvData.get("user_nm"),"")).append("</td>");
				htmlData.append("<th>사번</th><td>").append(StringUtils.defaultIfBlank(aprvData.get("user_id"),"")).append("</td></tr>");
				htmlData.append("<tr><th>부서</th><td>").append(StringUtils.defaultIfBlank(aprvData.get("dept_nm"),"")).append("</td>");
				htmlData.append("<th>직위</th><td>").append(StringUtils.defaultIfBlank(aprvData.get("pstn_nm"),"")).append("</td></tr>");
				htmlData.append("<tr><th>요청 사유</th><td colspan='3'>").append(StringUtils.defaultIfBlank(aprvData.get("rqst_resn"),"")).append("</td></tr>");
				htmlData.append("<tr><th>프로젝트명</th><td colspan='3'>").append(StringUtils.defaultIfBlank(aprvData.get("project_nm"),"")).append("</td></tr>");
				htmlData.append("<tr><th>프로젝트 리더</th><td colspan='3'>").append(StringUtils.defaultIfBlank(aprvData.get("mgr_info"),"")).append("</td></tr>");
				htmlData.append("<tr><th>프로젝트 참여자</th><td colspan='3'>").append(StringUtils.defaultIfBlank(aprvData.get("pcpt_info"),"")).append("</td></tr>");
				htmlData.append("<tr><th>프로젝트 기간</th><td colspan='3'>").append(StringUtils.defaultIfBlank(aprvData.get("project_pd"),"")).append("</td></tr>");
				htmlData.append("<tr><th>프로젝트 설명</th><td colspan='3'>").append(StringUtils.defaultIfBlank(aprvData.get("project_dsc"),"")).append("</td></tr>");
				htmlData.append("<tr><th>분석 환경 자원</th><td colspan='3'>").append(StringUtils.defaultIfBlank(aprvData.get("project_rsrc_app"),"")).append("</td></tr>");
				htmlData.append("<tr><th>트레이닝 자원</th><td colspan='3'>").append(StringUtils.defaultIfBlank(aprvData.get("project_rsrc_job"),"")).append("</td></tr>");
				htmlData.append("</tbody></table><p style='color:red'>* 프로젝트 수정 요청인 경우 이전 결재건 첨부</p>");
				htmlData.append("<br /><br />");
				htmlData.append("<table style='TABLE-LAYOUT:fixed;border-spacing:0px;'border=1>");
				htmlData.append("<colgroup><col style='width:20%' /><col style='width:40%' /><col style='width:40%' /></colgroup>");
				htmlData.append("<tbody><tr><th>번호</th><th>테이블영문명</th><th>테이블한글명</th></tr>");
				htmlData.append("<tr><th>1</th><td></td><td></td></tr>");
				htmlData.append("</tbody></table><p style='color:red'>* 전체 테이블 정보 첨부</p>");
				result.put(Constant.SocketServer.HTML_DATA, htmlData);
				
				result.put(Constant.SocketServer.RESULT, Constant.SocketServer.SUCCESS);
			} catch (Exception e) {
				log.warn("EAI Socket Sever 프로젝트 승인 DRAFTINIT 처리 중 오류");
				log.warn(e.getMessage());
			}
		}
		
		if (StringUtils.isBlank(result.optString(Constant.SocketServer.RESULT))) {
			setErrorMsg(result);
		}
		return result;
	}
	
	/**
	 * 프로젝트 승인 DRAFTSTART, DRAFTPROC, DRAFTEND, ...
	 * @param data
	 * @return
	 */
	@Transactional
	public JSONObject projectAprv(JSONObject data) {
		JSONObject result = new JSONObject();
		// 승인 정보 업데이트 및 후처리
		//반환 JSONObject
		//  Constant.SocketServer.RESULT    : 결과 / 성공 - "OK" / 실패 - "ERROR"
		//  Constant.SocketServer.ERROR_MSG : 오류설명 / 처리 실패인 경우 "전자결재 처리 중 오류가 발생하였습니다."
		//  Constant.SocketServer.HTML_DATA : 전자결재문서내용 HTML / NULL
		//  Constant.SocketServer.TITLE     : 전자결재제목   / NULL
		//  Constant.SocketServer.SUB_TITLE : 전자결재부제목 / NULL
		//  Constant.SocketServer.RCV_DEPT_CODE : 수신조직ID / NULL
		//  Constant.SocketServer.RCV_DEPT_NM   : 수신조직명 / NULL

		try {
			// 상세 등록 및 수정
			JSONArray aprvDtlInfo = data.getJSONArray(Constant.SocketServer.APRV_DTL_INFO);
			for (int i=0; i<aprvDtlInfo.length(); i++) {
				Map<String,Object> dtlInfo = aprvDtlInfo.getJSONObject(i).toMap();
				mapper.insertAprvRqstDtl(dtlInfo);
			}
			Map<String,Object> aprvInfo = data.toMap();
			if (StringUtils.equals(Constant.SocketServer.STAT_APRV,data.optString(Constant.SocketServer.APRV_STAT))) {
				//승인 처리
				mapper.updateAprvRqst(aprvInfo);
				// 후처리 : 프로젝트 정보 업데이트 및 이력 기록
				//승인 정보 조회
				AprvModel aprv = mapper.selectAprvRqst(data.getString(Constant.SocketServer.APRV_ID));
				// 프로젝트 승인 정보 업데이트
				mapper.updateProjectApply(aprv);
				// 프로젝트 정보 등록 또는 업데이트
				mapper.insertProject(aprv);
				// 프로젝트 이력 등록
				mapper.insertProjectHist(aprv);
				// 프로젝트 사용자 정보 등록
				mapper.insertProjectUser(aprv);
				// 프로젝트 사용자 정보 이력 등록
				mapper.insertProjectUserHist(aprv);
				
				// AWS 프로젝트 생성
				JSONObject params = new JSONObject();
				//프로젝트 ID
				params.put("teamId", aprv.getRefId());
				//사용자 목록
				List<String> userIds = new ArrayList<String>();
				ProjectModel project = mapper.selectProject(aprv.getRefId());
				JSONArray pcptList = project.getPcptInfo().optJSONArray("ids");
				for (int i=0; i<pcptList.length(); i++) {
					userIds.add(StringUtils.joinWith(null, "u",pcptList.optString(i)));
				}
				JSONArray mgrList = project.getMgrInfo().optJSONArray("ids");
				for (int i=0; i<mgrList.length(); i++) {
					userIds.add(StringUtils.joinWith(null, "u",mgrList.optString(i)));
				}
				params.put("userid_list", new JSONArray(userIds));
				// 프로젝트 자원
				// IDE 환경 자원 -> 분석 환경 자원
				JSONArray appList = project.getProjectRsrc().optJSONArray("appIds");
				params.put("allow_app_instance_type_list", appList);
				// 분석 실행 자원 -> 트레이닝 자원
				JSONArray jobList = project.getProjectRsrc().optJSONArray("jobIds");
				params.put("allow_job_instance_type_list", jobList);
				// 프로젝트 데이터
				JSONArray dataList = project.getProjectData().optJSONArray("tableList");
				JSONArray tableList = new JSONArray();
				for (int i=0; i<dataList.length(); i++) {
					String location = dataList.optJSONObject(i).optString("location");
					tableList.put(location.replaceAll("^([a-zA-z0-9]+://)",""));
				}
				params.put("table_list", tableList);
				if (StringUtils.equals("0.1", aprv.getRefVer())) {
					// 프로젝트 최초 등록
					awsLambdaUtil.invokeLambda(Constant.AWS.LambdaName.PROJECT_CREATE, params);
				} else {
					// 프로젝트 수정
					awsLambdaUtil.invokeLambda(Constant.AWS.LambdaName.PROJECT_UPDATE, params);
				}
			} else if (StringUtils.equals(Constant.SocketServer.STAT_REJECT,data.optString(Constant.SocketServer.APRV_STAT))) {
				//반려 처리
				mapper.updateAprvRqst(aprvInfo);
				// 후처리 없음
			} else {
				//결재중 처리
				// 후처리 없음
			}
			result.put(Constant.SocketServer.RESULT, Constant.SocketServer.SUCCESS);
		} catch (Exception e) {
			log.warn("EAI Socket Sever 프로젝트 승인 처리 중 오류");
			log.warn(e.getMessage());
		}
		
		if (StringUtils.isBlank(result.optString(Constant.SocketServer.RESULT))) {
			setErrorMsg(result);
		}
		return result;
	}
	
	/**
	 * 프로젝트 야간 사용 승인 DRAFTINIT
	 * @param data
	 * @return
	 */
	public JSONObject projectNightUseAprvFirst(JSONObject data) {
		JSONObject result = new JSONObject();
		//DB 조회 후 HTML 구성
		//반환 JSONObject
		//  Constant.SocketServer.RESULT    : 결과 / 성공 - "OK" / 실패 - "ERROR"
		//  Constant.SocketServer.ERROR_MSG : 오류설명 / 처리 실패인 경우 "전자결재 처리 중 오류가 발생하였습니다."
		//  Constant.SocketServer.HTML_DATA : 전자결재문서내용 HTML
		//  Constant.SocketServer.TITLE     : 전자결재제목   / "ML 프로젝트 야간 사용 신청"
		//  Constant.SocketServer.SUB_TITLE : 전자결재부제목 / "ML 프로젝트 야간 사용 신청"
		//  Constant.SocketServer.RCV_DEPT_CODE : 수신조직ID / "GS713"
		//  Constant.SocketServer.RCV_DEPT_NM   : 수신조직명 / "데이터사이언트팀"
		
		if (StringUtils.isNotBlank(data.optString(Constant.SocketServer.APRV_ID))) {
			try {
				//승인 기본 정보 조회
				List<Map<String,String>> aprvDefaultData = mapper.selectDefaultData(Constant.GoldwingAprvType.PROJECT_NIGHT_USE);
				for (Map<String,String> map : aprvDefaultData) {
					result.put(map.get("code_id"), map.get("code_nm"));
				}
				
				//승인 정보 조회
				Map<String,String> aprvData = mapper.selectProjectNightUseAprvData(data.optString(Constant.SocketServer.APRV_ID));
				StringBuilder htmlData = new StringBuilder();
				htmlData.append("<table style='TABLE-LAYOUT:fixed;border-spacing:0px;'border=1>");
				htmlData.append("<colgroup><col style='width:25%' /><col style='width:25%' /><col style='width:25%' /><col style='width:25%' /></colgroup>");
				htmlData.append("<tbody><tr><th colspan='4'>신청자 정보</th></tr>");
				htmlData.append("<tr><th>성명</th><td>").append(StringUtils.defaultIfBlank(aprvData.get("user_nm"),"")).append("</td>");
				htmlData.append("<th>사번</th><td>").append(StringUtils.defaultIfBlank(aprvData.get("user_id"),"")).append("</td></tr>");
				htmlData.append("<tr><th>부서</th><td>").append(StringUtils.defaultIfBlank(aprvData.get("dept_nm"),"")).append("</td>");
				htmlData.append("<th>직위</th><td>").append(StringUtils.defaultIfBlank(aprvData.get("pstn_nm"),"")).append("</td></tr>");
				htmlData.append("<tr><th>요청 사유</th><td colspan='3'>").append(StringUtils.defaultIfBlank(aprvData.get("rqst_resn"),"")).append("</td></tr>");
				htmlData.append("<tr><th>프로젝트명</th><td colspan='3'>").append(StringUtils.defaultIfBlank(aprvData.get("project_nm"),"")).append("</td></tr>");
				htmlData.append("<tr><th>프로젝트 리더</th><td colspan='3'>").append(StringUtils.defaultIfBlank(aprvData.get("mgr_info"),"")).append("</td></tr>");
				htmlData.append("<tr><th>프로젝트 기간</th><td colspan='3'>").append(StringUtils.defaultIfBlank(aprvData.get("project_pd"),"")).append("</td></tr>");
				htmlData.append("<tr><th>야간 사용 여부</th><td colspan='3'>").append(StringUtils.defaultIfBlank(aprvData.get("night_use_yn"),"")).append("</td></tr>");
				htmlData.append("<tr><th>야간 사용 기간</th><td colspan='3'>").append(StringUtils.defaultIfBlank(aprvData.get("night_use_pd"),"")).append("</td></tr>");
				htmlData.append("</tbody></table>");
				result.put(Constant.SocketServer.HTML_DATA, htmlData);
				
				result.put(Constant.SocketServer.RESULT, Constant.SocketServer.SUCCESS);
			} catch (Exception e) {
				log.warn("EAI Socket Sever 프로젝트 승인 DRAFTINIT 처리 중 오류");
				log.warn(e.getMessage());
			}
		}
		
		if (StringUtils.isBlank(result.optString(Constant.SocketServer.RESULT))) {
			setErrorMsg(result);
		}
		return result;
	}
	
	/**
	 * 프로젝트 야간 사용 승인 DRAFTSTART, DRAFTPROC, DRAFTEND, ...
	 * @param data
	 * @return
	 */
	@Transactional
	public JSONObject projectNightUseAprv(JSONObject data) {
		JSONObject result = new JSONObject();
		// 승인 정보 업데이트 및 후처리
		//반환 JSONObject
		//  Constant.SocketServer.RESULT    : 결과 / 성공 - "OK" / 실패 - "ERROR"
		//  Constant.SocketServer.ERROR_MSG : 오류설명 / 처리 실패인 경우 "전자결재 처리 중 오류가 발생하였습니다."
		//  Constant.SocketServer.HTML_DATA : 전자결재문서내용 HTML / NULL
		//  Constant.SocketServer.TITLE     : 전자결재제목   / NULL
		//  Constant.SocketServer.SUB_TITLE : 전자결재부제목 / NULL
		//  Constant.SocketServer.RCV_DEPT_CODE : 수신조직ID / NULL
		//  Constant.SocketServer.RCV_DEPT_NM   : 수신조직명 / NULL

		try {
			// 상세 등록 및 수정
			JSONArray aprvDtlInfo = data.getJSONArray(Constant.SocketServer.APRV_DTL_INFO);
			for (int i=0; i<aprvDtlInfo.length(); i++) {
				Map<String,Object> dtlInfo = aprvDtlInfo.getJSONObject(i).toMap();
				mapper.insertAprvRqstDtl(dtlInfo);
			}
			Map<String,Object> aprvInfo = data.toMap();
			if (StringUtils.equals(Constant.SocketServer.STAT_APRV,data.optString(Constant.SocketServer.APRV_STAT))) {
				//승인 처리
				mapper.updateAprvRqst(aprvInfo);
				// 후처리 : 프로젝트 야간 사용 정보 업데이트 및 이력 기록
				//승인 정보 조회
				AprvModel aprv = mapper.selectAprvRqst(data.getString(Constant.SocketServer.APRV_ID));
				//프로젝트 야간 사용 정보 업데이트
				Map<String,Object> map = aprv.getRqstInfo().toMap();
				map.put("projectId", aprv.getRefId());
				map.put("ver", aprv.getRefVer());
				map.put("roleSe", aprv.getRoleSe());
				map.put("refTy", aprv.getRefTy());
				map.put("userId", aprv.getRgstId());
				map.put("nightUseAprvStat", aprv.getAprvStat());
				mapper.updateProjectUserNightUse(map);
				mapper.insertProjectUserNightUseHist(map);
			} else if (StringUtils.equals(Constant.SocketServer.STAT_REJECT,data.optString(Constant.SocketServer.APRV_STAT))) {
				//반려 처리
				mapper.updateAprvRqst(aprvInfo);
				// 후처리 없음
			} else {
				//결재중 처리
				// 후처리 없음
			}
			result.put(Constant.SocketServer.RESULT, Constant.SocketServer.SUCCESS);
		} catch (Exception e) {
			log.warn("EAI Socket Sever 프로젝트 승인 처리 중 오류");
			log.warn(e.getMessage());
		}
		
		if (StringUtils.isBlank(result.optString(Constant.SocketServer.RESULT))) {
			setErrorMsg(result);
		}
		return result;
	}
	
	/**
	 * 사용자 권한 승인 - 일반사용자 -> 분석사용자 DRAFTINIT
	 * @param data
	 * @return
	 */
	public JSONObject userAuthAprvFirst(JSONObject data) {
		JSONObject result = new JSONObject();
		//DB 조회 후 HTML 구성
		//반환 JSONObject
		//  Constant.SocketServer.RESULT    : 결과 / 성공 - "OK" / 실패 - "ERROR"
		//  Constant.SocketServer.ERROR_MSG : 오류설명 / 처리 실패인 경우 "전자결재 처리 중 오류가 발생하였습니다."
		//  Constant.SocketServer.HTML_DATA : 전자결재문서내용 HTML
		//  Constant.SocketServer.TITLE     : 전자결재제목   / "사용자 권한 변경 요청"
		//  Constant.SocketServer.SUB_TITLE : 전자결재부제목 / "사용자 권한 변경 요청"
		//  Constant.SocketServer.RCV_DEPT_CODE : 수신조직ID / "GS713"
		//  Constant.SocketServer.RCV_DEPT_NM   : 수신조직명 / "데이터사이언트팀"
		
		if (StringUtils.isNotBlank(data.optString(Constant.SocketServer.APRV_ID))) {
			try {
				//승인 기본 정보 조회
				List<Map<String,String>> aprvDefaultData = mapper.selectDefaultData(Constant.GoldwingAprvType.AUTH_CHANGE);
				for (Map<String,String> map : aprvDefaultData) {
					result.put(map.get("code_id"), map.get("code_nm"));
				}
				
				//승인 정보 조회
				Map<String,String> aprvData = mapper.selectUserAuthAprvData(data.optString(Constant.SocketServer.APRV_ID));
				StringBuilder htmlData = new StringBuilder();
				htmlData.append("<table style='TABLE-LAYOUT:fixed;border-spacing:0px;'border=1>");
				htmlData.append("<colgroup><col style='width:25%' /><col style='width:25%' /><col style='width:25%' /><col style='width:25%' /></colgroup>");
				htmlData.append("<tbody><tr><th colspan='4'>신청자 정보</th></tr>");
				htmlData.append("<tr><th>성명</th><td>").append(StringUtils.defaultIfBlank(aprvData.get("user_nm"),"")).append("</td>");
				htmlData.append("<th>사번</th><td>").append(StringUtils.defaultIfBlank(aprvData.get("user_id"),"")).append("</td></tr>");
				htmlData.append("<tr><th>부서</th><td>").append(StringUtils.defaultIfBlank(aprvData.get("dept_nm"),"")).append("</td>");
				htmlData.append("<th>직위</th><td>").append(StringUtils.defaultIfBlank(aprvData.get("pstn_nm"),"")).append("</td></tr>");
				htmlData.append("<tr><th>요청 사유</th><td colspan='3'>").append(StringUtils.defaultIfBlank(aprvData.get("rqst_resn"),"")).append("</td></tr>");
				htmlData.append("<th colspan='2'>시스템 권한</th><th>변경 전</th><th>변경 후</th>");
				htmlData.append("<tr><th colspan='2'>사용자 시스템</th>");
				htmlData.append("<td>").append(StringUtils.defaultIfBlank(aprvData.get("user_bf_auth_nm"),"")).append("</td>");
				htmlData.append("<td>").append(StringUtils.defaultIfBlank(aprvData.get("user_af_auth_nm"),"")).append("</td></tr>");
				htmlData.append("<tr><th colspan='2'>관리자 시스템</th>");
				htmlData.append("<td>").append(StringUtils.defaultIfBlank(aprvData.get("mgr_bf_auth_nm"),"")).append("</td>");
				htmlData.append("<td>").append(StringUtils.defaultIfBlank(aprvData.get("mgr_af_auth_nm"),"")).append("</td></tr>");
				htmlData.append("</tbody></table>");
				result.put(Constant.SocketServer.HTML_DATA, htmlData);
				
				result.put(Constant.SocketServer.RESULT, Constant.SocketServer.SUCCESS);
			} catch (Exception e) {
				log.warn("EAI Socket Sever 사용자 권한 승인 DRAFTINIT 처리 중 오류");
				log.warn(e.getMessage());
			}
		}
		
		if (StringUtils.isBlank(result.optString(Constant.SocketServer.RESULT))) {
			setErrorMsg(result);
		}
		return result;
	}
	
	/**
	 * 사용자 권한 승인 - 일반사용자 -> 분석사용자 DRAFTSTART, DRAFTPROC, DRAFTEND, ...
	 * @param data
	 * @return
	 */
	@Transactional
	public JSONObject userAuthAprv(JSONObject data) {
		JSONObject result = new JSONObject();
		// 승인 정보 업데이트 및 후처리
		//반환 JSONObject
		//  Constant.SocketServer.RESULT    : 결과 / 성공 - "OK" / 실패 - "ERROR"
		//  Constant.SocketServer.ERROR_MSG : 오류설명 / 처리 실패인 경우 "전자결재 처리 중 오류가 발생하였습니다."
		//  Constant.SocketServer.HTML_DATA : 전자결재문서내용 HTML / NULL
		//  Constant.SocketServer.TITLE     : 전자결재제목   / NULL
		//  Constant.SocketServer.SUB_TITLE : 전자결재부제목 / NULL
		//  Constant.SocketServer.RCV_DEPT_CODE : 수신조직ID / NULL
		//  Constant.SocketServer.RCV_DEPT_NM   : 수신조직명 / NULL

		try {
			// 상세 등록 및 수정
			JSONArray aprvDtlInfo = data.getJSONArray(Constant.SocketServer.APRV_DTL_INFO);
			for (int i=0; i<aprvDtlInfo.length(); i++) {
				Map<String,Object> dtlInfo = aprvDtlInfo.getJSONObject(i).toMap();
				mapper.insertAprvRqstDtl(dtlInfo);
			}
			Map<String,Object> aprvInfo = data.toMap();
			if (StringUtils.equals(Constant.SocketServer.STAT_APRV,data.optString(Constant.SocketServer.APRV_STAT))) {
				//승인 처리
				mapper.updateAprvRqst(aprvInfo);
				// 후처리 : 사용자 권한 정보 업데이트 및 이력 기록
				//승인 정보 조회
				AprvModel aprv = mapper.selectAprvRqst(data.getString(Constant.SocketServer.APRV_ID));
				//사용자 시스템 권한 변경
				JSONObject userAuthInfo = aprv.getRqstInfo().getJSONObject("userAuth");
				if (StringUtils.isBlank(userAuthInfo.getString("afAuthId"))) {
					// 권한 제거
					mapper.deleteUserSysAuth(aprv.getRefId());
				} else {
					// 권한 변경
					Map<String,Object> map = userAuthInfo.toMap();
					map.put("userId", aprv.getRefId());
					mapper.updateMgrSysAuth(map);
				}
				
				//관리자 시스템 권한 변경
				JSONObject mgrAuthInfo = aprv.getRqstInfo().getJSONObject("mgrAuth");
				if (StringUtils.isBlank(mgrAuthInfo.getString("afAuthId"))) {
					// 권한 제거
					mapper.deleteMgrSysAuth(aprv.getRefId());
				} else {
					// 권한 등록 또는 변경
					Map<String,Object> map = mgrAuthInfo.toMap();
					map.put("userId", aprv.getRefId());
					mapper.updateMgrSysAuth(map);
				}
				// 사용자 이력 등록
				mapper.insertUserHist(aprv.getRefId());
				
			} else if (StringUtils.equals(Constant.SocketServer.STAT_REJECT,data.optString(Constant.SocketServer.APRV_STAT))) {
				//반려 처리
				mapper.updateAprvRqst(aprvInfo);
				// 후처리 없음
			} else {
				//결재중 처리
				// 후처리 없음
			}
			result.put(Constant.SocketServer.RESULT, Constant.SocketServer.SUCCESS);
		} catch (Exception e) {
			log.warn("EAI Socket Sever 사용자 권한 승인 처리 중 오류");
			log.warn(e.getMessage());
		}
		
		if (StringUtils.isBlank(result.optString(Constant.SocketServer.RESULT))) {
			setErrorMsg(result);
		}
		return result;
	}
	
	/**
	 * 데이터 권한 승인 DRAFTINIT
	 *     2021.03.22. JIRA에서 처리로 변경됨
	 * @param data
	 * @return
	 */
	public JSONObject dataAprvFirst(JSONObject data) {
		JSONObject result = new JSONObject();
		//DB 조회 후 HTML 구성
		//반환 JSONObject
		//  Constant.SocketServer.RESULT    : 결과 / 성공 - "OK" / 실패 - "ERROR"
		//  Constant.SocketServer.ERROR_MSG : 오류설명 / 처리 실패인 경우 "전자결재 처리 중 오류가 발생하였습니다."
		//  Constant.SocketServer.HTML_DATA : 전자결재문서내용 HTML
		//  Constant.SocketServer.TITLE     : 전자결재제목   / "데이터 자원 권한 요청"
		//  Constant.SocketServer.SUB_TITLE : 전자결재부제목 / "데이터 자원 권한 요청"
		//  Constant.SocketServer.RCV_DEPT_CODE : 수신조직ID / "GS713"
		//  Constant.SocketServer.RCV_DEPT_NM   : 수신조직명 / "데이터사이언트팀"
		
		if (StringUtils.isNotBlank(data.optString(Constant.SocketServer.APRV_ID))) {
			try {
				//승인 기본 정보 조회
				List<Map<String,String>> aprvDefaultData = mapper.selectDefaultData(Constant.GoldwingAprvType.DATA_RESOURCE);
				for (Map<String,String> map : aprvDefaultData) {
					result.put(map.get("code_id"), map.get("code_nm"));
				}
				
				//승인 정보 조회
				Map<String,String> aprvData = mapper.selectDataAprvData(data.optString(Constant.SocketServer.APRV_ID));
				StringBuilder htmlData = new StringBuilder();
				htmlData.append("<table style='TABLE-LAYOUT:fixed;border-spacing:0px;'border=1>");
				htmlData.append("<colgroup><col style='width:25%' /><col style='width:25%' /><col style='width:25%' /><col style='width:25%' /></colgroup>");
				htmlData.append("<tbody><tr><th colspan='4'>신청자 정보</th></tr>");
				htmlData.append("<tr><th>성명</th><td>").append(StringUtils.defaultIfBlank(aprvData.get("user_nm"),"")).append("</td>");
				htmlData.append("<th>사번</th><td>").append(StringUtils.defaultIfBlank(aprvData.get("user_id"),"")).append("</td></tr>");
				htmlData.append("<tr><th>부서</th><td>").append(StringUtils.defaultIfBlank(aprvData.get("dept_nm"),"")).append("</td>");
				htmlData.append("<th>직위</th><td>").append(StringUtils.defaultIfBlank(aprvData.get("pstn_nm"),"")).append("</td></tr>");
				htmlData.append("<tr><th>요청 사유</th><td colspan='3'>").append(StringUtils.defaultIfBlank(aprvData.get("rqst_resn"),"")).append("</td></tr>");
				htmlData.append("</tbody></table>");
				htmlData.append("<br /><br />");
				htmlData.append("<table style='TABLE-LAYOUT:fixed;border-spacing:0px;'border=1>");
				htmlData.append("<colgroup><col style='width:20%' /><col style='width:40%' /><col style='width:40%' /></colgroup>");
				htmlData.append("<tbody><tr><th>번호</th><th>테이블영문명</th><th>테이블한글명</th></tr>");
				htmlData.append("<tr><th>1</th><td></td><td></td></tr>");
				htmlData.append("</tbody></table><p style='color:red'>* 전체 테이블 정보 첨부</p>");
				result.put(Constant.SocketServer.HTML_DATA, htmlData);
				
				result.put(Constant.SocketServer.RESULT, Constant.SocketServer.SUCCESS);
			} catch (Exception e) {
				log.warn("EAI Socket Sever 데이터 권한 승인 DRAFTINIT 처리 중 오류");
				log.warn(e.getMessage());
			}
		}
		
		if (StringUtils.isBlank(result.optString(Constant.SocketServer.RESULT))) {
			setErrorMsg(result);
		}
		return result;
	}
	
	/**
	 * 데이터 권한 승인 DRAFTSTART, DRAFTPROC, DRAFTEND, ...
	 *     2021.03.22. JIRA에서 처리로 변경됨
	 * @param data
	 * @return
	 */
	@Transactional
	public JSONObject dataAprv(JSONObject data) {
		JSONObject result = new JSONObject();
		// 승인 정보 업데이트 및 후처리
		//반환 JSONObject
		//  Constant.SocketServer.RESULT    : 결과 / 성공 - "OK" / 실패 - "ERROR"
		//  Constant.SocketServer.ERROR_MSG : 오류설명 / 처리 실패인 경우 "전자결재 처리 중 오류가 발생하였습니다."
		//  Constant.SocketServer.HTML_DATA : 전자결재문서내용 HTML / NULL
		//  Constant.SocketServer.TITLE     : 전자결재제목   / NULL
		//  Constant.SocketServer.SUB_TITLE : 전자결재부제목 / NULL
		//  Constant.SocketServer.RCV_DEPT_CODE : 수신조직ID / NULL
		//  Constant.SocketServer.RCV_DEPT_NM   : 수신조직명 / NULL

		try {
			// 상세 등록 및 수정
			JSONArray aprvDtlInfo = data.getJSONArray(Constant.SocketServer.APRV_DTL_INFO);
			for (int i=0; i<aprvDtlInfo.length(); i++) {
				Map<String,Object> dtlInfo = aprvDtlInfo.getJSONObject(i).toMap();
				mapper.insertAprvRqstDtl(dtlInfo);
			}
			Map<String,Object> aprvInfo = data.toMap();
			if (StringUtils.equals(Constant.SocketServer.STAT_APRV,data.optString(Constant.SocketServer.APRV_STAT))) {
				//승인 처리
				mapper.updateAprvRqst(aprvInfo);
				// 후처리 : 데이터 권한 정보 업데이트
				
			} else if (StringUtils.equals(Constant.SocketServer.STAT_REJECT,data.optString(Constant.SocketServer.APRV_STAT))) {
				//반려 처리
				mapper.updateAprvRqst(aprvInfo);
				// 후처리 없음
			} else {
				//결재중 처리
				// 후처리 없음
			}
			result.put(Constant.SocketServer.RESULT, Constant.SocketServer.SUCCESS);
		} catch (Exception e) {
			log.warn("EAI Socket Sever 데이터 권한 승인 처리 중 오류");
			log.warn(e.getMessage());
		}
		
		if (StringUtils.isBlank(result.optString(Constant.SocketServer.RESULT))) {
			setErrorMsg(result);
		}
		return result;
	}
	
	/**
	 * 모델 배포 승인 DRAFTINIT
	 * @param data
	 * @return
	 */
	public JSONObject modelDeployAprvFirst(JSONObject data) {
		JSONObject result = new JSONObject();
		//DB 조회 후 HTML 구성
		//반환 JSONObject
		//  Constant.SocketServer.RESULT    : 결과 / 성공 - "OK" / 실패 - "ERROR"
		//  Constant.SocketServer.ERROR_MSG : 오류설명 / 처리 실패인 경우 "전자결재 처리 중 오류가 발생하였습니다."
		//  Constant.SocketServer.HTML_DATA : 전자결재문서내용 HTML
		//  Constant.SocketServer.TITLE     : 전자결재제목   / "ML 모델 배포 신청서"
		//  Constant.SocketServer.SUB_TITLE : 전자결재부제목 / "ML 모델 배포 신청서"
		//  Constant.SocketServer.RCV_DEPT_CODE : 수신조직ID / "GS713"
		//  Constant.SocketServer.RCV_DEPT_NM   : 수신조직명 / "데이터사이언트팀"
		
		if (StringUtils.isNotBlank(data.optString(Constant.SocketServer.APRV_ID))) {
			try {
				//승인 기본 정보 조회
				List<Map<String,String>> aprvDefaultData = mapper.selectDefaultData(Constant.GoldwingAprvType.MODEL_DEPLOYED);
				for (Map<String,String> map : aprvDefaultData) {
					result.put(map.get("code_id"), map.get("code_nm"));
				}
				
				//승인 정보 조회
				Map<String,String> aprvData = mapper.selectModelDeployAprvData(data.optString(Constant.SocketServer.APRV_ID));
				StringBuilder htmlData = new StringBuilder();
				htmlData.append("<table style='TABLE-LAYOUT:fixed;border-spacing:0px;'border=1>");
				htmlData.append("<colgroup><col style='width:25%' /><col style='width:25%' /><col style='width:25%' /><col style='width:25%' /></colgroup>");
				htmlData.append("<tbody><tr><th colspan='4'>신청자 정보</th></tr>");
				htmlData.append("<tr><th>성명</th><td>").append(StringUtils.defaultIfBlank(aprvData.get("user_nm"),"")).append("</td>");
				htmlData.append("<th>사번</th><td>").append(StringUtils.defaultIfBlank(aprvData.get("user_id"),"")).append("</td></tr>");
				htmlData.append("<tr><th>부서</th><td>").append(StringUtils.defaultIfBlank(aprvData.get("dept_nm"),"")).append("</td>");
				htmlData.append("<th>직위</th><td>").append(StringUtils.defaultIfBlank(aprvData.get("pstn_nm"),"")).append("</td></tr>");
				htmlData.append("<tr><th>요청 사유</th><td colspan='3'>").append(StringUtils.defaultIfBlank(aprvData.get("rqst_resn"),"")).append("</td></tr>");
				//항목 협의 중
				htmlData.append("</tbody></table>");
				result.put(Constant.SocketServer.HTML_DATA, htmlData);
				
				result.put(Constant.SocketServer.RESULT, Constant.SocketServer.SUCCESS);
			} catch (Exception e) {
				log.warn("EAI Socket Sever 데이터 권한 승인 DRAFTINIT 처리 중 오류");
				log.warn(e.getMessage());
			}
		}
		
		if (StringUtils.isBlank(result.optString(Constant.SocketServer.RESULT))) {
			setErrorMsg(result);
		}
		return result;
	}
	
	/**
	 * 모델 배포 승인 DRAFTSTART, DRAFTPROC, DRAFTEND, ...
	 * @param data
	 * @return
	 */
	@Transactional
	public JSONObject modelDeployAprv(JSONObject data) {
		JSONObject result = new JSONObject();
		// 승인 정보 업데이트 및 후처리
		//반환 JSONObject
		//  Constant.SocketServer.RESULT    : 결과 / 성공 - "OK" / 실패 - "ERROR"
		//  Constant.SocketServer.ERROR_MSG : 오류설명 / 처리 실패인 경우 "전자결재 처리 중 오류가 발생하였습니다."
		//  Constant.SocketServer.HTML_DATA : 전자결재문서내용 HTML / NULL
		//  Constant.SocketServer.TITLE     : 전자결재제목   / NULL
		//  Constant.SocketServer.SUB_TITLE : 전자결재부제목 / NULL
		//  Constant.SocketServer.RCV_DEPT_CODE : 수신조직ID / NULL
		//  Constant.SocketServer.RCV_DEPT_NM   : 수신조직명 / NULL

		try {
			// 상세 등록 및 수정
			JSONArray aprvDtlInfo = data.getJSONArray(Constant.SocketServer.APRV_DTL_INFO);
			for (int i=0; i<aprvDtlInfo.length(); i++) {
				Map<String,Object> dtlInfo = aprvDtlInfo.getJSONObject(i).toMap();
				mapper.insertAprvRqstDtl(dtlInfo);
			}
			Map<String,Object> aprvInfo = data.toMap();
			if (StringUtils.equals(Constant.SocketServer.STAT_APRV,data.optString(Constant.SocketServer.APRV_STAT))) {
				//승인 처리
				mapper.updateAprvRqst(aprvInfo);
				// 후처리 : 모델 배포 정보 업데이트
				
			} else if (StringUtils.equals(Constant.SocketServer.STAT_REJECT,data.optString(Constant.SocketServer.APRV_STAT))) {
				//반려 처리
				mapper.updateAprvRqst(aprvInfo);
				// 후처리 없음
			} else {
				//결재중 처리
				// 후처리 없음
			}
			result.put(Constant.SocketServer.RESULT, Constant.SocketServer.SUCCESS);
		} catch (Exception e) {
			log.warn("EAI Socket Sever 데이터 권한 승인 처리 중 오류");
			log.warn(e.getMessage());
		}
		
		if (StringUtils.isBlank(result.optString(Constant.SocketServer.RESULT))) {
			setErrorMsg(result);
		}
		return result;
	}
}
