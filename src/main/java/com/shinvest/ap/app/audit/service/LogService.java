package com.shinvest.ap.app.audit.service;

import com.shinvest.ap.app.audit.mapper.LogMapper;
import com.shinvest.ap.app.audit.model.LogModel;
import com.shinvest.ap.common.paging.Criteria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class LogService {

	@Resource
	private LogMapper logMapper;

	//사용자 접근로그 목록 추출
	public List<LogModel> selectUserLogList(Criteria criteria) {
		return logMapper.selectUserLogList(criteria);
	}

	//사용자 접근로그 카운트
	public int selectUserLogCount(Criteria criteria) {
		return logMapper.selectUserLogCount(criteria);
	}	
	
	//관리자 접근로그 목록 추출
	public List<LogModel> selectMgrLogList(Criteria criteria) {
		return logMapper.selectMgrLogList(criteria);
	}

	//관리자 접근로그 카운트
	public int selectMgrLogCount(Criteria criteria) {
		return logMapper.selectMgrLogCount(criteria);
	}

	//관리자 접근로그 생성 (t_log_rqst_mgr_sys)
	public long insert(LogModel model) {
		if(model != null) {
			return logMapper.insert(model);
		} else {
			return 0L;
		}
	}

	//관리자단 태블로 접근로그 생성 (t_log_tableau_mgr_sys) (※사용자단 태블로 접근로그는 t_log_tableau_user_sys에 쌓임)
	public long insertTableau(LogModel model) {
		if(model != null) {
			return logMapper.insertTableau(model);
		} else {
			return 0L;
		}
	}
}
