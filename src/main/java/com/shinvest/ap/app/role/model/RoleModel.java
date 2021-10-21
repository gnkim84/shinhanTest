package com.shinvest.ap.app.role.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 권한 데이터 모델
 */
@Data
public class RoleModel {

    private String rownum;              // 순번
    private String authId;              // 권한ID
    private String authCl;              // 권한분류
    private String authNm;              // 권한명
    private String authDsc;            // 권한설명
    private String useYn;               // 사용여부
    private String rgstId;              // 등록자 ID
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rgstDt;     // 등록일시
    private String modiId;              // 수정자 ID
    private LocalDateTime modiDt;     // 수정일시
    
    private String userId;
    private String refId;
    private String refTy;
}
