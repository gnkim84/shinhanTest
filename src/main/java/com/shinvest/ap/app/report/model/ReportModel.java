package com.shinvest.ap.app.report.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.json.JSONObject;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ReportModel implements Serializable {
    private String reportId;
    private String ver;
    private String minorVer;
    private String projectId;
    private String projectNm;
    private String reportNm;
    private String reportCl;
    private String reportTy;
    private String reportTyNm;
    private String reportSe;
    private JSONObject wrkCat;
    private String reportDsc;
    private String reportStat;
    private String reportStatNm;
    private String reportUrl;
    private JSONObject reportAttr;
    private String previewUrl;
    private String fileId;
    private String deptCode;
    private String deptNm;
    private JSONObject pcptInfo;
    private JSONObject mgrInfo;
    private JSONObject oldPcptInfo;
    private JSONObject oldMgrInfo;
    private JSONObject sanctnInfo;
    private String rqstResn;
    private String executCycle;
    private String activeYn;
    private String atmcAprvYn;
    private String dashboardUrl;
    private String report_ty;
    private String dept_code;
    private String wrk_cat;
    private int ordSeq;
    private String useYn;
    private String rgstId;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")    
    private LocalDateTime rgstDt;
    private String modiId;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")    
    private LocalDateTime modiDt;
    private String orgnReportId;
    private String orngReportVer;
    private JSONObject deptInfo;
    private String deptMngYn;
    private String refId;
    private String refTy;
    private String roleSe;
    private String authYn;
    private String userId;
    
    /*Tableau*/
    private String tableauWorkbookId;
    private String tableauProjectId;
    private String tableauViewId;
    
    private Integer rownum;             // 화면 순번
    
    private String mainPicrId;
    private String mainPicrNm;
    private String subPicrId;
    private String subPicrNm;
    private String mainPhotoUrl;
    private String subPhotoUrl;
}
