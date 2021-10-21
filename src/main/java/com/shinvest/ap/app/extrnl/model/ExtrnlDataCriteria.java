package com.shinvest.ap.app.extrnl.model;

import com.shinvest.ap.common.paging.Criteria;

import lombok.Data;

@Data
public class ExtrnlDataCriteria extends Criteria {
    private String dataId;
    private String filterDataTy;
    private String filterDataTrnsmisMthd;
    private String filterDataTrnsmisCycle;
}