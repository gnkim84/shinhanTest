package com.shinvest.ap.app.license.model;

import java.time.LocalDateTime;

import org.json.JSONObject;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

/**
 * 라이선스관리 모델
 */
@Data
public class LicenseModel {

    private String rownum;              // 순번
    private String authId;              // 권한ID
    private String authNm;              // 권한명
    private String licenseId;           // 라이선스ID
    private String tableauId;           // 태블로 계정
    private String tableauPw;          // 태블로 비밀번호
    private String awsId;               // AWS 계정
    private String awsPw;              // AWS 비밀번호
    private String licenseDsc;         // 라이센스설명
    private String useYn;               // 사용여부
    private String rgstId;              // 등록자ID
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rgstDt;     // 등록일시
    private String modiId;              // 수정자ID
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modiDt;     // 수정일시
    private String licenseTy;
    private String licenseSe;
    private String userId;
    private JSONObject tableauAttr;
    private JSONObject awsAttr;
    private String licenseCl;
    private String projectId;
    private String reportId;

}
