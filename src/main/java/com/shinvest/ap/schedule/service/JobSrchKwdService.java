package com.shinvest.ap.schedule.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.shinvest.ap.schedule.mapper.JobSrchKwdMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JobSrchKwdService {
	
	@Resource(name="jobSrchKwdMapper")
	private JobSrchKwdMapper mapper;

	@Transactional
	public void typeN() {
		// 최근 검색어 SRCH_CL = 'N'
		List<Map<String,Object>> data = mapper.selectNewKwd();
		if (data != null && data.size() > 7) {
			mapper.deleteNewKwd();
			Map<String,List<Map<String,Object>>> list = new HashMap<>();
			list.put("data", data);
			mapper.insertNewKwd(list);
		}
		
	}
	
	@Transactional
	public void typeM() {
		// 최다 검색어 SRCH_CL = 'M'
		List<Map<String,Object>> data = mapper.selectHitKwd();
		if (data != null && data.size() > 7) {
			mapper.deleteHitKwd();
			Map<String,List<Map<String,Object>>> list = new HashMap<>();
			list.put("data", data);
			mapper.insertHitKwd(list);
		}
	}
}
