package com.shinvest.ap.app.board.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class FreeModel implements Serializable {
	private String freeId;
	private String clCode;
	private String sj;
	private String cn;
	private String answ;
	private String fileId;
	private String answFileId;
	private String answRgstId;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") 
	private LocalDateTime answRgstDt;
	private String bfFreeId;
	private String useYn;
	private String rgstId;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") 
	private LocalDateTime rgstDt;
	private String modiId;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") 
	private LocalDateTime modiDt;
	private String freeStat;
	private String openYn;
	private int viewCnt;
	
	private String freeCateNm;
	private String freeStatNm;
	private String openYnNm;
	
	private int rownum;             // 화면 순번
    private String rgstNm;
    private String rgstDeptNm;
    private String rgstPstnNm;
	
	private String rgstPhotoUrl;	//등록자 사진
	
	private String[] fileIds;
}
