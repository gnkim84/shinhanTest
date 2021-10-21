package com.shinvest.ap.app.report.service;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.shinvest.ap.app.code.service.CodeService;
import com.shinvest.ap.app.report.mapper.ReportAuthMapper;
import com.shinvest.ap.app.report.model.ReportAuthModel;
import com.shinvest.ap.common.Constant;
import com.shinvest.ap.common.paging.Criteria;

@Service
public class ReportAuthService {
	
	@Resource
	private ReportAuthMapper reportAuthMapper;

	@Resource
	CodeService codeService;
	
	public List<ReportAuthModel> selectReportAuthList(Criteria criteria) {
		return reportAuthMapper.selectReportAuthList(criteria);
	}
	public int selectReportAuthListCount(Criteria criteria) {
		return reportAuthMapper.selectReportAuthListCount(criteria);
	}
	public ReportAuthModel selectReportAuth(ReportAuthModel model) {
		return reportAuthMapper.selectReportAuth(model);
	}
	
	@Transactional
	public String deleteReportAuth(ReportAuthModel model) {
		
		long count = reportAuthMapper.deleteReportAuth(model);
//		reportAuthMapper.insertReportAuthHst(model);
		
		if(count > 0) {
            return Constant.DB.INSERT;
        } else {
            return Constant.DB.FAIL;
        }
	}
	
	@Transactional
	public String insertReportAuth(ReportAuthModel model) {

		model.setVer(new BigDecimal("0.1").toString());
		model.setUseYn(Constant.YES);
		

		long count = reportAuthMapper.insertReportAuth(model);
//		reportAuthMapper.insertReportAuthHst(model);
		
		if(count > 0) {
            return Constant.DB.INSERT;
        } else {
            return Constant.DB.FAIL;
        }
	}
	
	@Transactional
	public String updateReportAuth(ReportAuthModel model) {
		
		/**
		 *  메이저 버전 업데이트/ 마이너 버전 업데이트 여부에 따른 JAVA 혹은 DB 상의 값 셋팅
		 * 올릴 버전 값을 세팅해주고 쿼리상에서 연산
		 */
		
		model.setVer(new BigDecimal("0.1").toString());											// 마이너
		model.setUseYn(Constant.YES);
		
//		model.setVer(new BigDecimal(model.getVer()).add(new BigDecimal("0.1")).toString());		// 마이너
//		model.setVer(new BigDecimal(model.getVer()).add(new BigDecimal("1")).toString());		// 메이저
		
		long count = reportAuthMapper.updateReportAuth(model);
//		reportAuthMapper.insertReportAuthHst(model);
		
		if(count > 0) {
            return Constant.DB.INSERT;
        } else {
            return Constant.DB.FAIL;
        }
	}

    /**
     * 모든 보고서 코드 가져오기
     * @param model
     */
	public void selectAllReportAuthCodeList(Model model) {

		model.addAttribute("codeRptTyList", codeService.selectGroupIdAllList("RPT_TY"));
    	
    }
	
}
