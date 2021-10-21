package com.shinvest.ap.app.report.mapper;

import java.util.List;

import com.shinvest.ap.app.report.model.ReportAuthModel;
import com.shinvest.ap.common.annotation.ConnMapperFirst;
import com.shinvest.ap.common.paging.Criteria;

@ConnMapperFirst
public interface ReportAuthMapper {

	List<ReportAuthModel> selectReportAuthList(Criteria criteria);
	int selectReportAuthListCount(Criteria criteria);
	ReportAuthModel selectReportAuth(ReportAuthModel model);
	long deleteReportAuth(ReportAuthModel model);
	long insertReportAuth(ReportAuthModel model);
	long updateReportAuth(ReportAuthModel model);
	
}
