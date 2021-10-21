package com.shinvest.ap.app.report.mapper;

import java.util.List;
import java.util.Map;

import com.shinvest.ap.app.project.model.ProjectCriteria;
import com.shinvest.ap.app.project.model.ProjectModel;
import com.shinvest.ap.app.report.model.ReportModel;
import com.shinvest.ap.common.annotation.ConnMapperFirst;
import com.shinvest.ap.common.paging.Criteria;

@ConnMapperFirst
public interface ReportMapper {

	List<ReportModel> selectReportList(Criteria criteria);
	int selectReportListCount(Criteria criteria);
	ReportModel selectReport(ReportModel model);
	long deleteReport(ReportModel model);
	long insertReport(ReportModel model);
	long updateReport(ReportModel model);
	List<ProjectModel> selectProjectList(ProjectCriteria criteria);
	
	long insertReportHst(ReportModel model);
	
	List<Map<String, String>> selectTableauWorkbookList();
	
	long insertReportUserRole(ReportModel model);

	long updateReportUserRole(ReportModel model);

	long deleteReportUserRole(ReportModel model);
	
	long insertReportUserRoleHist(ReportModel model);

	long insertSelectReportUserRoleHist(ReportModel model);
	
	ReportModel selectCheckReportUserRole(ReportModel model);

	List<ReportModel> selectReportUserRole(ReportModel model);
}
