package com.shinvest.ap.app.wrkCat.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 업무카테고리 모델
 */
@Data
public class WrkModel {

    private String rownum;              // 순번
    private String wrkId;               // 업무카테고리ID
    private String wrkNm;               // 업무카테고리명
    private String wrkDsc;             // 업무카테고리설명
    private String useYn;               // 사용여부
    private String rgstId;              // 등록자ID
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rgstDt;     // 등록일시
    private String modiId;              // 수정자ID
    private LocalDateTime modiDt;     // 수정일시
}
