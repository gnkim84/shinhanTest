package com.shinvest.ap.app.report.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ReportExtrnlModel implements Serializable {
/*
CREATE TABLE ptl.t_report_extrnl_aprv (
	aprv_id varchar(32) NOT NULL, -- 승인 ID
	report_id varchar(32) NULL, -- 보고서 ID
	ver numeric(9,3) NULL, -- 버전
	extrnl_id varchar(32) NULL, -- 외부 ID[권한 부여 대상 외부 시스템]
	rqst_dt timestamp NULL, -- 요청 일시
	aprv_stat varchar(8) NULL, -- 승인 상태
	aprv_dt timestamp NULL, -- 승인 일시
	aprvr_id varchar(32) NULL, -- 승인자 ID
	rqst_resn varchar(2000) NULL, -- 요청 사유
	rjct_resn varchar(2000) NULL, -- 반려 사유
	use_yn varchar(1) NULL DEFAULT 'N'::character varying, -- 사용 여부
	rgst_id varchar(32) NOT NULL, -- 등록 ID
	rgst_dt timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 등록 일시
	modi_id varchar(32) NOT NULL, -- 수정 ID
	modi_dt timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 수정 일시
	CONSTRAINT t_report_extrnl_aprv_pk PRIMARY KEY (aprv_id)
);
COMMENT ON TABLE ptl.t_report_extrnl_aprv IS '보고서 외부 승인[보고서 외부 배포 관리 정보]';
COMMENT ON COLUMN ptl.t_report_extrnl_aprv.aprv_id IS '승인 ID';
COMMENT ON COLUMN ptl.t_report_extrnl_aprv.report_id IS '보고서 ID';
COMMENT ON COLUMN ptl.t_report_extrnl_aprv.ver IS '버전';
COMMENT ON COLUMN ptl.t_report_extrnl_aprv.extrnl_id IS '외부 ID[권한 부여 대상 외부 시스템]';
COMMENT ON COLUMN ptl.t_report_extrnl_aprv.rqst_dt IS '요청 일시';
COMMENT ON COLUMN ptl.t_report_extrnl_aprv.aprv_stat IS '승인 상태';
COMMENT ON COLUMN ptl.t_report_extrnl_aprv.aprv_dt IS '승인 일시';
COMMENT ON COLUMN ptl.t_report_extrnl_aprv.aprvr_id IS '승인자 ID';
COMMENT ON COLUMN ptl.t_report_extrnl_aprv.rqst_resn IS '요청 사유';
COMMENT ON COLUMN ptl.t_report_extrnl_aprv.rjct_resn IS '반려 사유';
COMMENT ON COLUMN ptl.t_report_extrnl_aprv.use_yn IS '사용 여부';
COMMENT ON COLUMN ptl.t_report_extrnl_aprv.rgst_id IS '등록 ID';
COMMENT ON COLUMN ptl.t_report_extrnl_aprv.rgst_dt IS '등록 일시';
COMMENT ON COLUMN ptl.t_report_extrnl_aprv.modi_id IS '수정 ID';
COMMENT ON COLUMN ptl.t_report_extrnl_aprv.modi_dt IS '수정 일시';
*/
	//아래 3개는 부모테이블 필드들임
    private String 	projectId;
    private String 	projectNm;
    private String 	reportNm;
    private String 	extrnlNm;

	private String 	aprvId;
	private String 	reportId;
	private int 	ver;
	private String 	extrnlId;
	private String 	rqstDt;
	private String 	aprvStat;
	private String 	aprvDt;
	private String 	aprvrId;
	private String 	rqstResn;
	private String 	rjctResn;
	private String 	useYn;
	private String 	rgstId;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") 	
	private LocalDateTime 	rgstDt;
	private String 	modiId;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") 	
	private LocalDateTime 	modiDt;	
	
}
