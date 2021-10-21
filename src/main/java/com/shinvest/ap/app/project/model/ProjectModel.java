package com.shinvest.ap.app.project.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

import org.json.JSONObject;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Data
public class ProjectModel implements Serializable {
    private String projectId;
    private String ver;
    private String projectNm;
    private String projectCl;
    private String projectTy;
    private String projectSe;
    private String deptCode;
    private JSONObject pcptInfo;
    private JSONObject mgrInfo;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDt;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")    
    private LocalDate endDt;
    private String projectDsc;
    private String projectStat;
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
    private JSONObject projectData;
    private JSONObject deptInfo;
    private String tableauProjectId;
    private JSONObject projectRsrc;
    private String activeYn;
    private String lastVerYn;
    private String aprvStat;
    private String mainPicrId;
    private String subPicrId;
}
