package com.shinvest.ap.app.member.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.shinvest.ap.common.paging.Criteria;

/**
 * 페이징 처리 모델
 * 페이징 셋팅은 role.jsp파일 참고
 */
@Data
public class MemberCriteria extends Criteria {

    private String deptCode;
    private String fancytreeKey;
    private String deptNm;
    
    private String filterLockYn;
    
}
