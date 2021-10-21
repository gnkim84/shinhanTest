package com.shinvest.ap.app.aprv.model;

import java.time.LocalDateTime;

import org.json.JSONObject;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class AprvModel {

	private String aprvId;
	private String refId;
	private String refVer;
	private String aprvCl;
	private String aprvStat;
	private String aprvrId;
	private JSONObject rqstInfo;
	private String rqstResn;
	private String rjctResn;
	private String docId;
	private String useYn;
	private String rgstId;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime rgstDt;
	private String modiId;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime modiDt;
	private String roleSe;
	private String refTy;
	
	private int aprvSeq;
	private String aprvSe;
	private String aprvrNm;
	private String deptCode;
	private String deptNm;
	private String pstnCode;
	private String pstnNm;
}
