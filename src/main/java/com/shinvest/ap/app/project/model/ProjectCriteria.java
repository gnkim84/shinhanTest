package com.shinvest.ap.app.project.model;

import org.json.JSONObject;

import com.shinvest.ap.common.paging.Criteria;

import lombok.Data;

@Data
public class ProjectCriteria extends Criteria {
    private String projectTy;
    private String projectSe;
    private String projectStat;
    private JSONObject pcptInfo;
}
