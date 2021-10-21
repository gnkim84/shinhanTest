package com.shinvest.ap.app.report.mapper;

import java.util.List;

import com.shinvest.ap.app.report.model.ReportExtrnlModel;
import com.shinvest.ap.common.annotation.ConnMapperFirst;
import com.shinvest.ap.common.paging.Criteria;

@ConnMapperFirst
public interface ReportExtrnlMapper {

	List<ReportExtrnlModel> selectReportExtrnlList(Criteria criteria);
	int selectReportExtrnlListCount(Criteria criteria);
	ReportExtrnlModel selectReportExtrnl(ReportExtrnlModel model);
	long deleteReportExtrnl(ReportExtrnlModel model);
	long insertReportExtrnl(ReportExtrnlModel model);
	long updateReportExtrnl(ReportExtrnlModel model);
	
}
