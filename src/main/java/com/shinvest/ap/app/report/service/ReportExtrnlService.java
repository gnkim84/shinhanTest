package com.shinvest.ap.app.report.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shinvest.ap.app.report.mapper.ReportExtrnlMapper;
import com.shinvest.ap.app.report.model.ReportExtrnlModel;
import com.shinvest.ap.common.paging.Criteria;

@Service
public class ReportExtrnlService {
	
	@Resource
	private ReportExtrnlMapper reportExtrnlMapper;
	
	public List<ReportExtrnlModel> selectReportExtrnlList(Criteria criteria) {
		return reportExtrnlMapper.selectReportExtrnlList(criteria);
	}
	public int selectReportExtrnlListCount(Criteria criteria) {
		return reportExtrnlMapper.selectReportExtrnlListCount(criteria);
	}
	public ReportExtrnlModel selectReportExtrnl(ReportExtrnlModel model) {
		return reportExtrnlMapper.selectReportExtrnl(model);
	}
	public long deleteReportExtrnl(ReportExtrnlModel model) {
		return reportExtrnlMapper.deleteReportExtrnl(model);
	}
	public long insertReportExtrnl(ReportExtrnlModel model) {
		return reportExtrnlMapper.insertReportExtrnl(model);
	}
	public long updateReportExtrnl(ReportExtrnlModel model) {
		return reportExtrnlMapper.updateReportExtrnl(model);
	}

}
