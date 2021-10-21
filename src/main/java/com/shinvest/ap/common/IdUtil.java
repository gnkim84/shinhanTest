package com.shinvest.ap.common;

import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.shinvest.ap.common.service.IdService;

/**
 * ID 생성 Util
 * ID 생성 규칙 : 13자리
 *    ID 접두사 2자리 + 년도 2자리 + 구분자 + 1자리 + 연번 9자리
 *    연번 관리 테이블 : T_ID_SN
 */
@Component
public class IdUtil {

	@Resource(name="idService")
	private IdService service;
	
	/**
	 * UUID 생성
	 * @since2020.12.16.
	 * @return String 생성된 UUID
	 */
	public String getUUID() {
		return UUID.randomUUID().toString();
	}
	
	/**
	 * 파일 ID : 파일
	 */
	public String getFileId() {
		return service.getId(Constant.ID.FILE);
	}

	/**
	 * 공지사항 ID : 공지사항
	 */
	public String getNoticeId() {
		return service.getId(Constant.ID.NOTICE);
	}
	
	/**
	 * FAQ ID : FAQ
	 */
	public String getFaqId() {
		return service.getId(Constant.ID.FAQ);
	}
	
	/**
	 * QNA ID : QNA
	 */
	public String getQnaId() {
		return service.getId(Constant.ID.QNA);
	}
	
	/**
	 * 자유게시판 ID : 자유 게시판 ID
	 */
	public String getFreeId() {
		return service.getId(Constant.ID.FREE);
	}
	
	/**
	 * 권한 ID : 권한
	 */
	public String getAuthId() {
		return service.getId(Constant.ID.AUTH);
	}
	
	/**
	 * 메뉴 ID : 메뉴
	 */
	public String getMenuId() {
		return service.getId(Constant.ID.MENU);
	}
	
	/**
	 * 라이선스 ID : 라이선스
	 */
	public String getLicenseId() {
		return service.getId(Constant.ID.LICENSE);
	}
	
	/**
	 * 외부 ID : 외부 시스템
	 */
	public String getExtrnlId() {
		return service.getId(Constant.ID.EXTERNAL);
	}
	
	/**
	 * 외부 데이터 ID : 외부 데이터
	 */
	public String getExtrnlDataId() {
		return service.getId(Constant.ID.EXTERNAL_DATA);
	}	
	
	/**
	 * 업무 ID : 업무 카테고리
	 */
	public String getWrkId() {
		return service.getId(Constant.ID.WORK);
	}
	
	/**
	 * 프로젝트 ID : 프로젝트
	 */
	public String getProjectId() {
		return service.getId(Constant.ID.PROJECT);
	}
	
	/**
	 * 리포트 ID : 리포트
	 */
	public String getReportId() {
		return service.getId(Constant.ID.REPORT);
	}
	
	/**
	 * 승인 ID : 프로젝트 승인, 리포트 승인, 리포트 역할 승인, 리포트 외부 승인
	 */
	public String getAprvId() {
		return service.getId(Constant.ID.APPROVAL);
	}
	
	/**
	 * EAI 소켓 STND_COM_SEQ
	 */
	public String getEaiSocketStndComSeq() {
		return service.getTimeSeq(Constant.ID.EAI_SOCKET);
	}
}
